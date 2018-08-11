package com.github.dapeng.common.mail;

import com.github.dapeng.common.mail.entity.MailMsg;
import com.github.dapeng.common.mail.service.MailService;
import com.github.dapeng.common.mail.service.impl.ApacheMailServiceImpl;

/**
 * @author huyj
 * @Created 2018/5/4 16:49
 */
public class MailUtils {

    public static void sendEmail(String toUser,MailMsg msg) {
        MailService service = new ApacheMailServiceImpl();
        service.sendMail(toUser, msg);
    }
}
