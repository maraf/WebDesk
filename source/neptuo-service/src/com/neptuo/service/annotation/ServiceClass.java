/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Mara
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceClass {

    public String url() default "";
}
