# EasyMail
EasyMail is a library to send mail. You can send mail using a template html.


**The current stable release is 1.0**

##Features
* Gmail Server Support
* Outlook Server Support
* Html Template Support
* <span style="color:#00FF00;">Support for Attachments</span>

##Installation
**Add it in your root build.gradle at the end of repositories:**
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```



**Add the dependency in the build.gradle**
```
dependencies {
    compile 'com.github.ingyork:EasyMail:1.0'
}
```


##Usage

We can set EasyMail to work with servers Gmail or Outlook
* EasyMail.EASYMAIL_GMAIL
* EasyMail.EASYMAIL_OUTLOOK


Simple Setup
```
new EasyMail().create(this, new EMGmail())
        .defaultConfiguration()
        .addCredentials(MailKey.EMAIL, MailKey.PASSWORD)
        .addMailTo(mail1)
        .addSubject("Message Subject")
        .addBody("This is a Message Body")
```



Other configurations
```
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
```
*replace `MailKey.EMAIL` and `MailKey.PASSWORD` by the user credentials*




**With the code below we send an mail with html format. We use a HTML Template**
```
HashMap<String, String> htmlValue = new HashMap<>();
htmlValue.put("{{title}}", "This is the title of mail");
htmlValue.put("{{messageTop}}", "This is a small title of mail");
htmlValue.put("{{content}}", "This is the content of mail");

new EasyMail().create(this, new EMOutlook())
        .defaultConfiguration()
        .showProgress(true)
        .setProgressData("Sending", "Sending message, please wait...")
        .addCredentials(MailKey.EMAIL, MailKey.PASSWORD)
        .addMailTo(mail1+','+mail2)
        .setHtmlFilePath("test.html")
        .addSubject("This is a mail with html format")
        .setUseHtmlFormat(true)
        .setHtmlValue(htmlValue)
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
```


To use the html template you must create an object type HashMap <String, String> where the key is the name of the variable in the html content.

you must set `setUseHtmlFormat` as  true and pass as parameter the hashmap in the method `setHtmlValue(hashMap)`. additionally must specify the name of the html file in the method `setHtmlFilePath('template.html')`, this should be in the folder asset

Asset folder

![alt image Asset folder](http://image.prntscr.com/image/31a8d17d5e1240bf8d8cd6bcd78b11b4.png "Asset Folder")


Define your html template in the *Assets folder of your proyect*. Add variables with the following structure {{variable}} to be replaced by your content.
Then you can see an example of a html template.

![alt image of mail template](http://image.prntscr.com/image/1861c9e85c5b49e79d8c32d71c3e0941.png "Description")

###Result
![alt html result](http://image.prntscr.com/image/9bda0f118b64439db05c218767d587b6.png "Html Result")



##Licence
Copyright (c) 2016 Jorge Luis Castro Medina

This software is distributed under Apache 2.0 license. You can get a copy of the license
[Licence Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)