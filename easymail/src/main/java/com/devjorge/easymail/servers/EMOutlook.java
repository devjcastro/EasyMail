package com.devjorge.easymail.servers;

import com.devjorge.easymail.EasyMailBuilder;

import java.util.Properties;

/**
 * Created by jorge.castro on 24/06/2016.
 */
public class EMOutlook extends EasyMailBuilder {

    Properties properties = null;

    @Override
    public void defaultConfiguration() {

        properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.live.com");
        properties.put("mail.smtp.port", "587"); //If the port is omitted. Default is port 25

        email.setProperties(properties);
    }
}
