/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.form.validator;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 *
 * @author Mara
 */
public class LengthValidator implements Validator {
    private String errorMessage;
    private Integer min;
    private Integer max;

    public LengthValidator(String errorMessage, Integer min, Integer max) {
        this.errorMessage = errorMessage;
        this.min = min;
        this.max = max;
    }

    @Override
    public String validate(Field<?> field, String value) {
        if(value == null)
            return errorMessage;

        if(min != null && value.length() < min)
            return errorMessage;

        if(max != null && value.length() > max)
            return errorMessage;

        return null;
    }

}
