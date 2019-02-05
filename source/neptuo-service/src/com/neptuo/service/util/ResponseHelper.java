/*
 * To change this template; break; choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.util;

import com.neptuo.service.HttpParamsProvider;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
public class ResponseHelper {

    public static class MimeTypes {
        public static String JSON = "application/json";
        public static String XML = "application/xml";
    };

    public static class Codes {
        public static int CONTINUE = 100;
        public static int OK = 200;
        public static int CREATED = 201;
        public static int ACCEPTED = 202;
        public static int NON_AUTHORATIVE_INFORMATION = 203;
        public static int NO_CONTENT = 204;
        public static int RESET_CONTENT = 205;
        public static int PARTIAL_CONTENT = 206;
        public static int MULTIPLE_CHICES = 300;
        public static int MOVED_PERMANENTLY = 301;
        public static int FOUND = 302;
        public static int SEE_OTHER = 303;
        public static int NOT_MODIFIED = 304;
        public static int USE_PROXY = 305;
        public static int TEMPORARY_REDIRECT = 307;
        public static int BAD_REQUEST = 400;
        public static int UNAUTHORIZED = 401;
        public static int PAYMENT_REQUIRED = 402;
        public static int FORBIDDEN = 403;
        public static int NOT_FOUND = 404;
        public static int METHOD_NOT_ALLOWED = 405;
        public static int NOT_ACCEPTABLE = 406;
        public static int PROXY_AUTHENTICATION_REQUIRED = 407;
        public static int REQUEST_TIMEOUT = 408;
        public static int CONFLICT = 409;
        public static int GONE = 410;
        public static int LENGTH_REQUIRED = 411;
        public static int PRECONDITION_FAILED = 412;
        public static int REQUEST_ENTITY_TOO_LARGE = 413;
        public static int REQUEST_URI_TOO_LONG = 414;
        public static int UNSUPPORTED_MEDIA_TYPE = 415;
        public static int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        public static int EXPECTATION_FAILED = 417;
        public static int INTERNAL_SERVER_ERROR = 500;
        public static int NOT_IMPLEMENTED = 501;
        public static int BAD_GATEWAY = 502;
        public static int SERVICE_UNAVAILABLE = 503;
        public static int GATEWAY_TIMEOUT = 504;
        public static int HTTP_VERSION_NOT_SUPPORTED = 505;
    };

    /**
     * Returns string message for http status code
     *
     * @param code http status code
     * @return message for passed code
     */
    public static String getStatusMessage(int code) {
        String message = "";
        switch (code) {
            case 100:
                message = "Continue";
                break;
            case 101:
                message = "Switching Protocols";
                break;
            case 200:
                message = "OK";
                break;
            case 201:
                message = "Created";
                break;
            case 202:
                message = "Accepted";
                break;
            case 203:
                message = "Non-Authoritative Information";
                break;
            case 204:
                message = "No Content";
                break;
            case 205:
                message = "Reset Content";
                break;
            case 206:
                message = "Partial Content";
                break;
            case 300:
                message = "Multiple Choices";
                break;
            case 301:
                message = "Moved Permanently";
                break;
            case 302:
                message = "Found";
                break;
            case 303:
                message = "See Other";
                break;
            case 304:
                message = "Not Modified";
                break;
            case 305:
                message = "Use Proxy";
                break;
            case 306:
                message = "(Unused)";
                break;
            case 307:
                message = "Temporary Redirect";
                break;
            case 400:
                message = "Bad Request";
                break;
            case 401:
                message = "Unauthorized";
                break;
            case 402:
                message = "Payment Required";
                break;
            case 403:
                message = "Forbidden";
                break;
            case 404:
                message = "Not Found";
                break;
            case 405:
                message = "Method Not Allowed";
                break;
            case 406:
                message = "Not Acceptable";
                break;
            case 407:
                message = "Proxy Authentication Required";
                break;
            case 408:
                message = "Request Timeout";
                break;
            case 409:
                message = "Conflict";
                break;
            case 410:
                message = "Gone";
                break;
            case 411:
                message = "Length Required";
                break;
            case 412:
                message = "Precondition Failed";
                break;
            case 413:
                message = "Request Entity Too Large";
                break;
            case 414:
                message = "Request-URI Too Long";
                break;
            case 415:
                message = "Unsupported Media Type";
                break;
            case 416:
                message = "Requested Range Not Satisfiable";
                break;
            case 417:
                message = "Expectation Failed";
                break;
            case 500:
                message = "Internal Server Error";
                break;
            case 501:
                message = "Not Implemented";
                break;
            case 502:
                message = "Bad Gateway";
                break;
            case 503:
                message = "Service Unavailable";
                break;
            case 504:
                message = "Gateway Timeout";
                break;
            case 505:
                message = "HTTP Version Not Supported";
                break;
        }
        return message;
    }

    /**
     * Returns whole string message for http status code
     *
     * @param code http status code
     * @return whole message for passed code
     */
    public static String getWholeStatusMessage(int code) {
        return "HTTP/1.1 " + code + " " + getStatusMessage(code);
    }
}
