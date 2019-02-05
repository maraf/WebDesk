/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class Feedback extends BaseModelData {

    public Feedback() {
        modelType.setRecordName("feedback");
        modelType.addField("email");
        modelType.addField("subject");
        modelType.addField("content");
    }

    public Feedback(String email, String subject, String content) {
        this();
        setEmail(email);
        setSubject(subject);
        setContent(content);
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public String getEmail() {
        return get("email");
    }

    public void setSubject(String subject) {
        set("subject", subject);
    }

    public String getSubject() {
        return get("subject");
    }

    public void setContent(String content) {
        set("content", content);
    }

    public String getContent() {
        return get("content");
    }
}
