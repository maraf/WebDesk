package com.neptuo.service.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.neptuo.service.annotation.RequestInput;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author Mara
 */
public class ReflectionHelper {
    private static Reflections reflections;

    public static Class getGenericParameterType(Method m, int index) {
        Type[] genericParameterTypes = m.getGenericParameterTypes();

        Type genericParameterType = genericParameterTypes[index];
        if (genericParameterType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericParameterType;
            Type[] parameterArgTypes = aType.getActualTypeArguments();
            for (Type parameterArgType : parameterArgTypes) {
                Class parameterArgClass = (Class) parameterArgType;
                return parameterArgClass;
            }
        }
        return null;
    }

    public static RequestInput getParamRequestInputAnnotation(Method m, int index) {
        Annotation[] anns = m.getParameterAnnotations()[index];
        for (Annotation a : anns) {
            if (a instanceof RequestInput) {
                return (RequestInput) a;
            }
        }
        return null;
    }

    public static boolean hasParamRequestInputAnnotation(Method m, int index) {
        return getGenericParameterType(m, index) == null;
    }

    public static <T extends Annotation> T getParamAnnotation(Method m, int index, Class<T> type) {
        Annotation[] anns = m.getParameterAnnotations()[index];
        for (Annotation a : anns) {
            if (type.isAssignableFrom(a.getClass())) {
                return (T) a;
            }
        }
        return null;
    }

    public static <T extends Annotation> T getAnnotation(Method m, Class<T> type) {
        Annotation[] anns = m.getAnnotations();
        for (Annotation a : anns) {
            if (type.isAssignableFrom(a.getClass())) {
                return (T) a;
            }
        }
        return null;
    }

    public static Set<URL> getClasspathUrls(ServletContext context) {
        String baseUrl = null;
        Set<URL> urls = new HashSet<URL>();
        Set libJars = context.getResourcePaths("/WEB-INF/lib");
        for (Object jar : libJars) {
            try {
                String jarUrl = (String) jar;
                URL url = context.getResource(jarUrl);
                urls.add(url);
                baseUrl = url.toString().substring(0, url.toString().indexOf("/WEB-INF"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        if (baseUrl != null) {
            try {
                URL url = new URL(baseUrl + "/WEB-INF/classes/");
                urls.add(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return urls;
    }

    public static Reflections getReflections(ServletContext context) {
        if (reflections == null) {
            reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.getUrlsForPackagePrefix("com.neptuo.service")).setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner()));
            Reflections reflections1 = new Reflections(new ConfigurationBuilder().setUrls(ReflectionHelper.getClasspathUrls(context)).setScanners(new TypeAnnotationsScanner()));
            reflections = reflections.merge(reflections1);
        }
        return reflections;
    }
}
