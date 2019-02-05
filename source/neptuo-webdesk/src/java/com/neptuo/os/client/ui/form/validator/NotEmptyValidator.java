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
public class NotEmptyValidator implements Validator {
    private String errorMessage;

    public NotEmptyValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String validate(Field<?> field, String value) {
        if(field.getValue() == null || field.getValue().equals("")) {
            return errorMessage;
        }
        return null;
    }

}
