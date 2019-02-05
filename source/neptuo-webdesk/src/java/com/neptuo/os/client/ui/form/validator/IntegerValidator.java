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
public class IntegerValidator implements Validator {
    private String errorMessage;

    public IntegerValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String validate(Field<?> field, String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return errorMessage;
        }
        return null;
    }

}
