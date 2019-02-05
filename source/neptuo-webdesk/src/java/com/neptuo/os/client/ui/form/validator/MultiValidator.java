/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.form.validator;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MultiValidator implements Validator {
    private List<Validator> validators = new ArrayList<Validator>();

    public MultiValidator(Validator ... validators) {
        this.validators.addAll(Arrays.asList(validators));
    }

    @Override
    public String validate(Field<?> field, String value) {
        for(Validator validator : validators) {
            String result = validator.validate(field, value);
            if(result != null) {
                return result;
            }
        }
        return null;
    }

}
