/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.locale;

/**
 * Use the following code to "instantiate" this interface:
 * {@code WebdeskConstants i18n = GWT.create(WebdeskConstants.class);}
 * See this URL for more examples:
 * http://code.google.com/intl/en-EN/webtoolkit/doc/latest/DevGuideI18nConstants.html
 *
 * @author Mara
 */
public interface WebdeskConstants extends com.google.gwt.i18n.client.Constants {

    String webdesktitle();
    String username();
    String usernameNotValid();
    String password();
    String passwordNotValid();
    String passwordAgain();
    String email();
    String subject();
    String login();
    String logout();
    String timeoutIn();
    String timeoutInCaption();
    String domain();
    String name();
    String surname();
    String created();
    String enabled();
    String message();
    String dateTime();

    String loggingIn();
    String loadingProperties();

    String errorSendingRequest();
    String errorRetrievingdata();

    String loginNoUser();

    String id();
    String open();
    String openNewWindow();
    String save();
    String ok();
    String yes();
    String cancel();
    String select();
    String send();

    String type();

    String integerValidator();
    String notEmptyValidator();

    String refresh();
    String minimize();
    String close();

    String feedback();
    String logger();

    String feedback_sent();
}
