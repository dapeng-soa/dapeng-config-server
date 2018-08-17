package com.github.dapeng.common.mail.service.impl;

import com.github.dapeng.common.mail.MailCfg;
import com.github.dapeng.common.mail.entity.MailAttach;
import com.github.dapeng.common.mail.entity.MailMsg;
import com.github.dapeng.common.mail.service.MailService;
import org.apache.commons.mail.*;

import java.util.ArrayList;
import java.util.List;



public class ApacheMailServiceImpl implements MailService {

    public void sendMail(String toEmail, MailMsg mailMsg) {
        if(toEmail.equals("")){
            sendMail(MailCfg.DEFAULT_FROM_EMAIL, MailCfg.DEFAULT_FROM_PASSWD,
                    MailCfg.DEFAULT_FROM_NAME,MailCfg.HOST,MailCfg.DEFAULT_TO_NAME, mailMsg);
        }else{
            sendMail(MailCfg.DEFAULT_FROM_EMAIL,MailCfg.DEFAULT_FROM_PASSWD,
                    MailCfg.DEFAULT_FROM_NAME,MailCfg.HOST,toEmail, mailMsg);
        }

    }

    public void sendMail(List<String> toEmailList, MailMsg mailMsg) {
        sendMail(MailCfg.DEFAULT_FROM_EMAIL,MailCfg.DEFAULT_FROM_PASSWD,
                MailCfg.DEFAULT_FROM_NAME,MailCfg.HOST,toEmailList,mailMsg);
    }

    public void sendMail(String fromEmail, String fromPasswd,String fromName,
                         String host,String toEmail, MailMsg mailMsg) {
        List<String> toEmailList = new ArrayList<String>();
        String [] to = toEmail.split(",");
        for(int i=0;i<to.length;i++){
            toEmailList.add(to[i]);
        }
        sendMail(fromEmail, fromPasswd,fromName,host,toEmailList, mailMsg);
    }

    public void sendMail(String fromEmail, String fromPasswd,String fromName,
                         String host,List<String> toEmailList,MailMsg mailMsg) {
        switch (mailMsg.getType()) {
            case text:
                sendTextMail(fromEmail, fromPasswd, fromName,host, toEmailList, mailMsg);
                break;

            case html:
                sendHtmlMail(fromEmail, fromPasswd, fromName,host, toEmailList, mailMsg);
                break;

            case multi:
                sendMultiMail(fromEmail, fromPasswd, fromName,host, toEmailList, mailMsg);
                break;

            default:
                break;
        }
    }


    //发送文本邮件
    private void sendTextMail(String fromEmail, String fromPasswd,String fromName,
                              String host,List<String> toEmailList,MailMsg mailMsg){
        SimpleEmail email = new SimpleEmail();
        try {
            initEmail(email, fromEmail, fromPasswd,fromName, host, toEmailList, mailMsg);
            email.setMsg(mailMsg.getContent());
            email.send();
        }
        catch (EmailException e) {
            e.printStackTrace();
        }
    }

    //发送html邮件
    private void sendHtmlMail(String fromEmail, String fromPasswd,String fromName,
                              String host,List<String> toEmailList,MailMsg mailMsg){
        HtmlEmail email = new HtmlEmail();
        try {
            initEmail(email, fromEmail, fromPasswd, fromName,host, toEmailList, mailMsg);
            email.setHtmlMsg(mailMsg.getContent());
            email.send();
        }
        catch (EmailException e) {
            e.printStackTrace();
        }
    }

    //发送复合邮件
    private void sendMultiMail(String fromEmail, String fromPasswd,String fromName,
                               String host,List<String> toEmailList,MailMsg mailMsg){
        HtmlEmail email = new HtmlEmail();

        try {
            initEmail(email, fromEmail, fromPasswd,fromName, host, toEmailList, mailMsg);
            email.setHtmlMsg(mailMsg.getContent());

            //添加附件
            List<MailAttach> attachList = mailMsg.getAttachList();
            EmailAttachment attachment = null;
            for (MailAttach mailAttach : attachList) {
                attachment = new EmailAttachment();
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(mailAttach.getName());
                attachment.setDescription(mailAttach.getDescription());
                attachment.setPath(mailAttach.getPath());
                attachment.setURL(mailAttach.getUrl());

                email.attach(attachment);
            }

            email.send();
        }
        catch (EmailException e) {
            e.printStackTrace();
        }
    }

    //初始化email发送信息
    private void initEmail(Email email, String fromEmail, String fromPasswd, String fromName,
                           String host, List<String> toEmailList, MailMsg mailMsg) throws EmailException{
        email.setHostName(host);
        String conf[] = host.split(":");
        if(conf.length >= 1){
            email.setHostName(conf[0]);
        }
        if(conf.length >= 2){
            email.setSmtpPort(Integer.parseInt(conf[1]));
        }
        //邮件服务器验证：用户名/密码
        email.setAuthentication(fromEmail, fromPasswd);
        //必须放在前面，否则乱码
        email.setCharset(MailCfg.CHARSET);
        email.setDebug(false);//是否开启调试默认不开启
        email.setSSLOnConnect(false);//开启SSL加密
        email.setStartTLSEnabled(false);//开启TLS加密

        email.addTo(toEmailList.toArray(new String[0]));
        email.setFrom(fromEmail,fromName);
        email.setSubject(mailMsg.getSubject());
    }

}
