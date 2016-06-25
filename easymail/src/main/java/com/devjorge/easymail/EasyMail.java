package com.devjorge.easymail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.devjorge.easymail.util.MailSender;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
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
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by jorge.castro on 16/06/2016.
 */
public class EasyMail {

    private Session session;
    private Context context;
    private EasyMailBuilder easyMailBuilder;


    private OnSuccess onSuccessCallback;
    private OnFail onFail;


    private ProgressDialog progress;
    private String progressTitle;
    private String progressMessage;


    private boolean isVisibleProgress;
    private boolean useHtmlFormat;
    private String htmlFilePath;
    private HashMap<String, String> htmlValue;



    //private Multipart multipart;
    private List<String> attachments;


    /*Interfaces*/
    public interface OnSuccess{
        void onSuccess();
    }

    public interface OnFail{
        void onFail(String failMessage);
        void onError(String errorMessage);
    }




    private void initByDefault(Context context){

        if(context != null) {
            this.progress = new ProgressDialog(context);
            this.progressTitle = "Sending";
            this.progressMessage = "Sending message, please wait...";
        }

        //multipart = new MimeMultipart();
        attachments = new ArrayList<>();

    }



    public EasyMail create(EasyMailBuilder mailServer){
        return new EasyMail(mailServer);
    }

    public EasyMail create(Context context, EasyMailBuilder mailServer){
        return new EasyMail(context, mailServer);
    }


    public EasyMail(){
        initByDefault(null);
    }

    private EasyMail(EasyMailBuilder mailServer) {
        this.context = null;
        this.easyMailBuilder = mailServer;

        initByDefault(context);
    }


    private EasyMail(Context context, EasyMailBuilder mailServer){
        this.context = context;
        this.easyMailBuilder = mailServer;

        initByDefault(context);

    }


    public void setContext(Context context){
        this.context = context;
    }

    public void setEasyMailBuilder(EasyMailBuilder easyMailBuilder){
        this.easyMailBuilder = easyMailBuilder;
    }




    public EasyMail showProgress(boolean status){
        this.isVisibleProgress = status;
        return this;
    }

    public EasyMail setProgressData(String title, String message){
        this.progressTitle = title;
        this.progressMessage = message;
        return this;
    }


    public EasyMail addCredentials(String username, String password){
        this.easyMailBuilder.email.addCredentials(username, password);
        return this;
    }


    public EasyMail defaultConfiguration(){
        this.easyMailBuilder.defaultConfiguration();
        return this;
    }



    public boolean isUseHtmlFormat() {
        return this.useHtmlFormat;
    }

    public EasyMail setUseHtmlFormat(boolean useHtmlFormat) {
        this.useHtmlFormat = useHtmlFormat;
        return this;
    }

    public String getHtmlFilePath() {
        return htmlFilePath;
    }

    public EasyMail setHtmlFilePath(String htmlFilePath) {
        this.htmlFilePath = htmlFilePath;
        return this;
    }





    public HashMap<String, String> getHtmlValue() {
        return htmlValue;
    }


    public EasyMail setHtmlValue(HashMap<String, String> htmlValue) {
        this.htmlValue = htmlValue;
        return this;
    }





    /*Add subject*/
    public EasyMail addSubject(String subject){
        this.easyMailBuilder.email.setSubject(subject);
        return this;
    }

    /*Add body*/
    public EasyMail addBody(String body){
        this.easyMailBuilder.email.setBody(body);
        return this;
    }

    /*Add mailTo*/
    public EasyMail addMailTo(String mailTo){
        this.easyMailBuilder.email.setMailTo(mailTo);
        return this;
    }



    public EasyMail setOnSuccess(OnSuccess onSuccessCallback){
        this.onSuccessCallback = onSuccessCallback;
        return this;
    }

    public EasyMail setOnFail(OnFail onFail){
        this.onFail = onFail;
        return this;
    }


    public EasyMail addAttachments(List<String> attachments){
        this.attachments = attachments;
        return this;
    }


    public EasyMail addAttachment(String filePath){
        this.attachments.add(filePath);
        return this;
    }











    public void sendMail(){

		if(easyMailBuilder == null){
			throw new IllegalArgumentException("You didn't set a server");
		}
        if(TextUtils.isEmpty(easyMailBuilder.email.getUsername()) || TextUtils.isEmpty(easyMailBuilder.email.getPassword())){
           throw new IllegalArgumentException("The user credentials are required for authentication");
        }

        if(TextUtils.isEmpty(getHtmlFilePath()) && useHtmlFormat){
            throw new IllegalArgumentException("You didn't set a htmlFilePath");
        }

        if(TextUtils.isEmpty(easyMailBuilder.email.getMailTo())){
            throw new IllegalArgumentException("You didn't set a recipient");
        }

        if(TextUtils.isEmpty(easyMailBuilder.email.getSubject())){
            throw new IllegalArgumentException("You didn't set a subject");
        }

        if(TextUtils.isEmpty(easyMailBuilder.email.getBody()) && htmlValue.size() == 0){
            throw new IllegalArgumentException("You didn't set a message body");
        }

        new EasyMailAsyncTask().execute();

    }







    private class ProcessStatus{

        public static final String PROCCESS_SUCCESS = "success";
        public static final String PROCCESS_FAIL = "fail";
        public static final String PROCCESS_ERROR = "error";


        private String status;
        private String message;
        private Exception exception;

        public ProcessStatus(String status, String message, Exception e){
            this.status = status;
            this.exception = e;
        }

        public void setExceptionData(String status, Exception e){
            this.status = status;
            this.exception = e;
        }

        public void setStatusData(String status, String message){
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



    private String getHtmlMessage() throws IOException{

        String html = "";

        if(htmlFilePath != null && htmlValue.size() > 0){

            StringBuilder out = new StringBuilder();

            InputStream is = context.getAssets().open(htmlFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            html = out.toString();

            for(String key : htmlValue.keySet()){
                html = html.replace(key, htmlValue.get(key));
            }

        }
        else{
            html = easyMailBuilder.email.getBody();
        }


        return html;
    }




    /*
    private void addAttachment(String filePath, String fileName) throws MessagingException {

        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(new File(filePath));
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

    }
    */



    public class EasyMailAsyncTask extends AsyncTask<Void, Void, ProcessStatus>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(isVisibleProgress) {
                progress.setTitle(progressTitle);
                progress.setMessage(progressMessage);
                progress.show();
            }


        }


        @Override
        protected ProcessStatus doInBackground(Void... params) {

            ProcessStatus processStatus = new ProcessStatus(ProcessStatus.PROCCESS_FAIL, "", null);

            //Session.getDefaultInstance()
            /*
            session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(easyMailBuilder.email.getUsername(), easyMailBuilder.email.getPassword());
                        }
                    });
            */

            try{


                String subject = easyMailBuilder.email.getSubject();

                MailSender sender = new MailSender(
                        easyMailBuilder.email.getUsername(),
                        easyMailBuilder.email.getPassword(),
                        easyMailBuilder.email.getProperties());


                if(!attachments.isEmpty()){
                    sender.addAttachments(attachments);
                }

                if(useHtmlFormat) {

                    sender.sendMailHtmlTemplate(subject, getHtmlMessage(),
                            easyMailBuilder.email.getUsername(),
                            easyMailBuilder.email.getMailTo());
                }
                else {

                    sender.sendMail(subject, easyMailBuilder.email.getBody(),
                            easyMailBuilder.email.getUsername(),
                            easyMailBuilder.email.getMailTo(),
                            MailSender.FORMATTING_TEXTPLAIN);
                }



                //Sending email
                //Transport.send(mm);
                processStatus.setStatus(ProcessStatus.PROCCESS_SUCCESS);

            }
            catch (AuthenticationFailedException e){
                processStatus.setStatusData(ProcessStatus.PROCCESS_ERROR, "Authentication failed");
            }
            catch (MessagingException e){
                processStatus.setStatusData(ProcessStatus.PROCCESS_ERROR, e.getMessage());
            }
            catch (Exception e){
                processStatus.setStatusData(ProcessStatus.PROCCESS_ERROR, e.getMessage());
            }


            return processStatus;
        }


        @Override
        protected void onPostExecute(ProcessStatus processStatus) {
            super.onPostExecute(processStatus);


            if(processStatus.getStatus().equals(ProcessStatus.PROCCESS_SUCCESS)){
                onSuccessCallback.onSuccess();
            }
            else if(processStatus.getStatus().equals(ProcessStatus.PROCCESS_FAIL)){
                onFail.onFail(processStatus.getMessage());
            }
            else if(processStatus.getStatus().equals(ProcessStatus.PROCCESS_ERROR)){
                onFail.onError(processStatus.getMessage());
            }

            progress.dismiss();

        }
    }


}
