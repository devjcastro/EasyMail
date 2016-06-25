package com.devjorge.easymail;

import java.util.Properties;

/**
 * Created by jorge.castro on 24/06/2016.
 */
public class Email {

    /*User credentials*/
    private String username;
    private String password;
    private String mailServer;

    private String mailTo;
    private String subject;
    private String body;

    private Properties properties;


    public void addCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {

        if(body == null)
            body = "";

        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
