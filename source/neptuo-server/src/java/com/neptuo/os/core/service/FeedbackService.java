/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.os.core.MailException;
import com.neptuo.os.core.data.model.User;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServerResource;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/core/feedback")
public class FeedbackService {

    @ServiceMethod
    public void handleFeedback(HttpServletRequest request, User current, @RequestInput("feedback") Feedback feedback, @ServerResource("/WEB-INF/mail.properties") InputStream mail)
            throws MailException, HttpNotAcceptableException, IOException {
        if (feedback != null) {
            Properties props = new Properties();
            props.load(mail);
            sendEmail(props, feedback.getEmail(), "neptuo-os feedback", buildContent(current, feedback));
        } else {
            throw new HttpNotAcceptableException("Missing feedback information");
        }
    }

    public String buildContent(User current, Feedback feedback) {
        return "From: " + feedback.getEmail() + ", UserID: " + current.getUsername() 
                + " [" + current.getId() + "]<br />Date: " + (new Date()) + "<br />Subject: "
                + feedback.getSubject() + "<hr />" + feedback.getContent();
    }

    public void sendEmail(Properties mailProperties, String receiver, String subject, String content) throws MailException {
        try {
            String host = mailProperties.getProperty("neptuo.os.mail.host");
            int port = Integer.valueOf(mailProperties.getProperty("neptuo.os.mail.port"));
            String user = mailProperties.getProperty("neptuo.os.mail.user");
            String password = mailProperties.getProperty("neptuo.os.mail.password");
            if (receiver == null) {
                receiver = mailProperties.getProperty("neptuo.os.mail.feedback.receiver");
            }

            Session mailSession = Session.getDefaultInstance(mailProperties);

            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setContent(content, "text/html");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

            transport.connect(host, port, user, password);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception ex) {
            throw new MailException(ex);
        }
    }
}
