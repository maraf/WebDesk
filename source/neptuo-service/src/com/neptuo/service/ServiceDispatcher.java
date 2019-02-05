/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.result.RawResult;
import com.neptuo.service.io.AutoSerializer;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.JsonSerializer;
import com.neptuo.service.io.Serializer;
import com.neptuo.service.io.XmlDeserializer;
import com.neptuo.service.io.XmlSerializer;
import com.neptuo.service.util.ResponseHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 *
 * @author Mara
 */
public class ServiceDispatcher extends HttpServlet {

    private static final Logger log = Logger.getLogger(ServiceDispatcher.class.getName());
    //@-Resource
    private UserTransaction transaction;
    //ServiceMap
    private ServiceMap services;
    //Service method ParamsProviders
    private MethodParamsProviders paramsProviders;

    /**
     * Initializes ServiceMap for serving requests
     *
     * @param url current request url
     * @param name method name
     * @param methodType method type
     * @return service request info for current request
     * @throws HttpException HttpException when service isn't found
     */
    protected ServiceRequestInfo findService(String url, String name, HttpMethodType methodType) throws HttpException {
        //Find service
        log.log(Level.INFO, "Request to " + url + ", method " + name + ", using http method " + methodType);
        if (services.containsKey(url)) {
            ServiceInfo service = services.get(url);
            if (service != null && service.getMethods().containsKey(name)) {
                //Find method
                MethodInfo method = service.getMethods().get(name);
                if (method != null) {
                    //Check HttpMethodType
                    if (Arrays.asList(method.getAnnotation().httpMethod()).contains(methodType)) {
                        return new ServiceRequestInfo(service, method);
                    }
                }
            }
        }

        //Service at passed url and method/methodType isn't registered
        throw new HttpNotFoundException();
    }

    /**
     * Parses request url and substracts sevice url
     *
     * @param request current http request
     * @return service url
     */
    protected String parseServiceUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String path = request.getContextPath() + request.getServletPath();

        if (url.length() >= path.length()) {
            return url.substring(path.length());
        }
        return "";
    }

    /**
     * Selects correct implementation of Serializer for current request
     *
     * @param request current request
     * @param response current response
     * @return Serializer implementation
     */
    protected Serializer getSerializer(HttpServletRequest request, HttpServletResponse response) {
        Serializer serializer;
        if (request.getHeader("Accept").contains(ResponseHelper.MimeTypes.XML)) {
            response.setContentType(ResponseHelper.MimeTypes.XML);
            serializer = new XmlSerializer();
        } else {
            response.setContentType(ResponseHelper.MimeTypes.JSON);
            serializer = new JsonSerializer();
        }
        return serializer;
    }

    /**
     * Selects correct implementation of Deserializer for current response
     * Now we support only XmlDeserializer
     *
     * @param request current request
     * @param response current response
     * @return Deserializer implementation
     */
    protected Deserializer getDeserializer(HttpServletRequest request, HttpServletResponse response) {
        return new XmlDeserializer();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response, HttpMethodType.DELETE);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response, HttpMethodType.PUT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response, HttpMethodType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response, HttpMethodType.POST);
    }

    /**
     * Holds all requests for service
     *
     * @param request current request
     * @param response current response
     * @param methodType http request type
     * @throws IOException
     */
    protected void doService(HttpServletRequest request, HttpServletResponse response, HttpMethodType methodType) throws IOException {
        try {

            //Initialize ServiceMap
            if (services == null) {
                services = new ServiceMap();
                services.initialize(getServletContext());
            }
            //Initialize ParamsProviders
            if (paramsProviders == null) {
                paramsProviders = new MethodParamsProviders();
                paramsProviders.initialize(getServletContext());
            }

            //Get "ServiceMethod"-header value, if not set, try to find method without name (with plain name)
            String methodHeader = request.getHeader("Method");
            if (methodHeader == null) {
                methodHeader = "";
            }

            //Get current request service info
            ServiceRequestInfo info = findService(parseServiceUrl(request), methodHeader, methodType);

            //Create transaction
            transaction = (UserTransaction) new InitialContext().lookup("UserTransaction");
            transaction.begin();

            //Create serializer & deserializer
            Serializer serializer = getSerializer(request, response);
            Deserializer deserializer = getDeserializer(request, response);

            //Call "BeforeRequest" on providers
            paramsProviders.runBefore(getServletContext(), request, response, serializer, deserializer);

            //Instantiate
            Object service = info.getService().getClazz().newInstance();

            //TODO: Use roleProvider
            if (info.getMethod().getAnnotation().require().length > 0) {
                //Test, that current user has all required roles
                boolean found = false;
                for (String roleName : info.getMethod().getAnnotation().require()) {
                    found = paramsProviders.runAuthorize(roleName);
                    if (found) {
                        break;
                    }
                }
                if (!found) {
                    throw new HttpUnauthorizedException();
                }
            }

            //Create initializer
            ParamsInitializer pi = new ParamsInitializer(paramsProviders, request.getInputStream(), info.getMethod(), deserializer);

            //Call service method
            Object result = info.getMethod().getMethod().invoke(service, pi.getParams());

            //Serialize result if any
            ResultSerializer rs = new ResultSerializer(result, serializer, response);
            if (result != null) {
                if (RawResult.class.isAssignableFrom(result.getClass())) {
                    //Write result as raw byte to output stream (eg. images)
                    response.getOutputStream().write(((RawResult) result).getRawResult());
                } else {
                    //Write as string
                    response.getWriter().write(rs.getRawResult());
                    response.setContentLength(rs.getRawResult().length());
                }
            } else {
                response.setContentLength(0);
            }

            //Commit transaction
            transaction.commit();
        } catch (HttpException ex) {
            //Set response status
            response.setStatus(ex.getHttpCode());

            //Serialize exception to output
            serializeException(request, response, response.getWriter(), ex);

            log.log(Level.SEVERE, null, ex);
            rollback();
        } catch (InvocationTargetException ex) {
            //Set response status
            if (HttpException.class.isAssignableFrom(ex.getTargetException().getClass())) {
                response.setStatus(((HttpException) ex.getTargetException()).getHttpCode());
            } else {
                response.setStatus(ResponseHelper.Codes.INTERNAL_SERVER_ERROR);
            }

            //Serialize exception to output
            serializeException(request, response, response.getWriter(), ex.getTargetException());

            log.log(Level.SEVERE, null, ex);
            rollback();
        } catch (Exception ex) {
            //Set response status
            response.setStatus(ResponseHelper.Codes.INTERNAL_SERVER_ERROR);

            //Serialize exception to output
            serializeException(request, response, response.getWriter(), ex);

            log.log(Level.SEVERE, null, ex);
            rollback();
        } finally {
            try {
                //Call "AfterRequest" on providers
                paramsProviders.runAfter(getServletContext(), request, response);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Rollback current transaction, if any
     */
    private void rollback() {
        try {
            if (transaction != null && transaction.getStatus() == Status.STATUS_ACTIVE) {
                transaction.rollback();
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Serializes exception
     *
     * @param request current request
     * @param response current response
     * @param writer response writer
     * @param e exception to serialize
     */
    private void serializeException(HttpServletRequest request, HttpServletResponse response, PrintWriter writer, Throwable e) {
        if (e != null) {
            Serializer s = getSerializer(request, response);
            AutoSerializer.factory(e, s).serialize();
            writer.write(s.getResult());
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        // Call "Destroy" on providers
        if (paramsProviders != null) {
            paramsProviders.destroy();
        }
    }

    @Override
    public String getServletInfo() {
        return "ServiceDispatcher";
    }
}
