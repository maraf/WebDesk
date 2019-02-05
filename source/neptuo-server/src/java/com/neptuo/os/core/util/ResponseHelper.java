/*
 * To change this template; break; choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.util;

import com.neptuo.os.core.service.CoreParamsProvider;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
public class ResponseHelper {

    public static void setAuthToken(HttpServletResponse response, String value) {
        response.setHeader(CoreParamsProvider.AUTH_HEADER_NAME, value);
    }
}
