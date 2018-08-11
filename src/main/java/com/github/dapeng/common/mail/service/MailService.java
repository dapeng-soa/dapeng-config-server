package com.github.dapeng.common.mail.service;


import com.github.dapeng.common.mail.entity.MailMsg;

import java.util.List;


/**
 * 
		* <p>Title: 邮件发送服务</p>
		* <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 */
public interface MailService {
	/**
	 * 
			*@name 邮件发送，默认发送者
			*@Description  
	 */
	void sendMail(String toEmail, MailMsg mailMsg);
	
	/**
	 * 
			*@name 邮件群发，默认发送者
			*@Description  
	 */
	void sendMail(List<String> toEmailList, MailMsg mailMsg);
	
	/**
	 * 
			*@name 邮件发送
			*@Description  
	 */
	void sendMail(String fromEmail, String fromPasswd, String fromName,
                  String host, String toEmail, MailMsg mailMsg);
	
	/**
	 * 
			*@name 邮件群发
			*@Description  
	 */
	void sendMail(String fromEmail, String fromPasswd, String fromName,
                  String host, List<String> toEmailList, MailMsg mailMsg);
}
