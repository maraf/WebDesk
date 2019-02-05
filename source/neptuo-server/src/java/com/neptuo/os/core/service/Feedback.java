/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.service;

import com.neptuo.service.io.annotation.Deserializable;

/**
 *
 * @author Mara
 */
@Deserializable(name="feedback")
public class Feedback {
    private String email;
    private String subject;
    private String content;

    public Feedback() {
    }

    public String getContent() {
        return content;
    }

    @Deserializable(name="content")
    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    @Deserializable(name="email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    @Deserializable(name="subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
