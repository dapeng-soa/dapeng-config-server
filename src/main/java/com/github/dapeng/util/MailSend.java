package com.github.dapeng.util;

import com.github.dapeng.common.mail.MailUtils;
import com.github.dapeng.common.mail.entity.MailMsg;
import com.github.dapeng.common.mail.enums.MailMsgType;
import com.github.dapeng.entity.ApiKeyInfo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author with struy.
 * Create by 2018/8/12 03:34
 * email :yq1724555319@gmail.com
 */

public class MailSend {
    /**
     * 下发ApiKey邮件
     *
     * @param keyInfo
     * @param sendBy
     */
    public static void sendApiKeyMail(ApiKeyInfo keyInfo, String sendBy) {
        MailMsg msg = new MailMsg();
        msg.setSubject(keyInfo.getBiz() + "-网关ApiKey下发");
        msg.setType(MailMsgType.text);
        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append("ApiKey: ").append(keyInfo.getApiKey()).append("\n");
        sb.append("Password: ").append(keyInfo.getPassword()).append("\n");
        sb.append("验证超时时间: ").append(keyInfo.getValidated() == 0 ? "是" : "否").append("\n");
        sb.append("网关超时时间: ").append(keyInfo.getTimeout()).append("s\n");
        sb.append("适用范围: ").append(keyInfo.getBiz()).append("\n");
        sb.append("下发日期: ").append(now.toString()).append("\n");
        sb.append("生效时间: ").append(now.toString()).append(" ").append(time.toString()).append("\n");
        sb.append("有效期: ").append("永久有效").append("\n");
        sb.append("备注: ").append("\n");
        sb.append(keyInfo.getNotes()).append("\n");
        sb.append("下发说明: ").append("\n");
        sb.append("现为").append(keyInfo.getBiz()).append("，分配当前ApiKey，请妥善保存！如需去除超时验证请回复说明。").append("\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("--- 本邮件由中台配置中心下发!");
        msg.setContent(sb.toString());
        MailUtils.sendEmail(sendBy, msg);
    }
}
