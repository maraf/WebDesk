/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.annotation;

import com.neptuo.service.HttpMethodType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Mara
 */
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceMethod {

    public String name() default "";

    public HttpMethodType[] httpMethod() default {HttpMethodType.POST};

    public String[] require() default {};
}
