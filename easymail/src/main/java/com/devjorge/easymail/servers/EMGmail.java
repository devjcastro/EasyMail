package com.devjorge.easymail.servers;

import com.devjorge.easymail.EasyMailBuilder;

import java.util.Properties;

/**
 * Created by jorge.castro on 24/06/2016.
 */
public class EMGmail extends EasyMailBuilder {

    Properties properties = null;

    @Override
    public void defaultConfiguration() {

        properties = new Properties();

        //Configuring properties for gmail
        /*
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        */

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.quitwait", "false");

        email.setProperties(properties);

    }

}
