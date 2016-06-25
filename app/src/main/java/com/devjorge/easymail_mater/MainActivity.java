package com.devjorge.easymail_mater;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devjorge.easymail.servers.EMGmail;
import com.devjorge.easymail.servers.EMOutlook;
import com.devjorge.easymail.EasyMail;
import com.devjorge.easymail_mater.app.MailKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String mail1 = "tes-mail@gmail.com";
    private String mail2 = "tes-mail@outlook.com";


    private Button btnSendMail;
    private Button btnSendMail2;
    private Button btnSendMailHtml;

    private List<String> attachments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendMail = (Button) findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener(this);


        btnSendMail2 = (Button) findViewById(R.id.btnSendMail2);
        btnSendMail2.setOnClickListener(this);

        btnSendMailHtml = (Button)findViewById(R.id.btnSendMailHtml);
        btnSendMailHtml.setOnClickListener(this);


        String fileName = "android-lollipop-tim-roes-webinar-12-2014.pdf";
        String filePath = Environment.getExternalStorageDirectory().toString()+"/"+Environment.DIRECTORY_DOWNLOADS+"/"+fileName;
        attachments = new ArrayList<>();
        for(int i=0; i<5; i++)
            attachments.add(filePath);


    }

    @Override
    public void onClick(View v) {
        if(v == btnSendMail){
            sendMail();
        }
        else if(v == btnSendMail2){
            sendMultipleRecipients();
        }
        else if(v == btnSendMailHtml){
            sendMailHtml();
        }
    }



    /*Send simple mail*/
    private void sendMail(){


        new EasyMail().create(this, new EMGmail())
                .defaultConfiguration()
                .addCredentials(MailKey.EMAIL, MailKey.PASSWORD)
                .addMailTo(mail1)
                .addSubject("Message Subject")
                .addBody("This is a Message Body")
                .addAttachment(attachments.get(0))
                .setOnSuccess(new EasyMail.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Mail sending success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnFail(new EasyMail.OnFail() {
                    @Override
                    public void onFail(String failMessage) {
                        Toast.makeText(MainActivity.this, "Fail: "+failMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                })
                .sendMail();
    }


    private void sendMultipleRecipients(){

        new EasyMail().create(this, new EMGmail())
                .defaultConfiguration()
                .showProgress(true)
                .setProgressData("Sending", "Sending message, please wait...")
                .addCredentials(MailKey.EMAIL, MailKey.PASSWORD)
                .addMailTo(mail1+','+mail2)
                .addSubject("This is a Mail with multiple recipients")
                .addBody("This is a Message Body")
                .setOnSuccess(new EasyMail.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Mail sending success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnFail(new EasyMail.OnFail() {
                    @Override
                    public void onFail(String failMessage) {
                        Toast.makeText(MainActivity.this, "Fail: "+failMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                })
                .sendMail();


    }



    /*Send Mail wiht Html Template*/
    private void sendMailHtml(){

        HashMap<String, String> htmlValue = new HashMap<>();
        htmlValue.put("{{title}}", "This is the title of mail");
        htmlValue.put("{{messageTop}}", "This is a small title of mail");
        htmlValue.put("{{content}}", "This is the content of mail");

        new EasyMail().create(this, new EMGmail())
                .defaultConfiguration()
                .showProgress(true)
                .setProgressData("Sending", "Sending message, please wait...")
                .addCredentials(MailKey.EMAIL, MailKey.PASSWORD)
                .addMailTo(mail1+','+mail2)
                .setHtmlFilePath("test.html")
                .addSubject("This is a mail with html format")
                .setUseHtmlFormat(true)
                .setHtmlValue(htmlValue)
                .addAttachments(attachments)
                .setOnSuccess(new EasyMail.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Mail sending success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnFail(new EasyMail.OnFail() {
                    @Override
                    public void onFail(String failMessage) {
                        Toast.makeText(MainActivity.this, "Fail: "+failMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                })
                .sendMail();


    }


}
