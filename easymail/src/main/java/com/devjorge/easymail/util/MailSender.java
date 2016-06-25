package com.devjorge.easymail.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by jorge.castro on 25/06/2016.
 */
public class MailSender extends Authenticator {

    public static final String FORMATTING_TEXTPLAIN = "text/plain";
    public static final String FORMATTING_HTML_UTF8 = "text/html; charset=utf-8";

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    private Multipart multipart;


    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSender(String user, String password, Properties props) {
        this.user = user;
        this.password = password;
        //session = Session.getDefaultInstance(props, this);
        session = Session.getInstance(props, this);
        multipart = new MimeMultipart();
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }


    public void addAttachments(List<String> attachments) throws MessagingException {
        if(!attachments.isEmpty()){
            File file = null;
            for(String filePath : attachments){
                file = new File(filePath);
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
            }
        }
    }


    public synchronized void sendMail(String subject, String body, String sender, String recipients, String formatting) throws Exception {

        if(formatting == null)
            formatting = FORMATTING_TEXTPLAIN;

        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), formatting));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


        if(multipart.getCount() > 0){
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);
            //Send the complete message part
            message.setContent(multipart);
        }
        else{
         message.setText(body);
        }


        Transport.send(message);
    }



    public synchronized void sendMailHtmlTemplate(String subject, String body, String sender, String recipients) throws Exception {

        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes()));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


        if(multipart.getCount() > 0){
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, FORMATTING_HTML_UTF8);
            multipart.addBodyPart(messageBodyPart);
            //Send the complete message part
            message.setContent(multipart);
        }
        else{
            message.setText(body);
        }


        Transport.send(message);
    }




    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

}
