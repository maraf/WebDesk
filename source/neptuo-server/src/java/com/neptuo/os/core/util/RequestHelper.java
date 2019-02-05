/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.util;

import com.neptuo.os.core.service.CoreParamsProvider;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mara
 */
public class RequestHelper {
    
    public static String getAuthToken(HttpServletRequest request) {
        return request.getHeader(CoreParamsProvider.AUTH_HEADER_NAME);
    }
}
