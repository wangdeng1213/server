package com.linksus.common.util;

import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.linksus.entity.Email;
import com.linksus.entity.LinksusRelationEmailConf;
import com.linksus.entity.MailProperty;

/**
 * 
 * email工具类
 * 
 * @author dxliud
 * 
 */
public class MailUtil{

	private JavaMailSender sendMail;

	public void setSendMail(JavaMailSender sendMail){
		this.sendMail = sendMail;
	}

	/**
	 * 发送邮件
	 * 
	 * @param toMails
	 *            收件人
	 * @param subject
	 *            邮件主题
	 * @param text
	 *            邮件内容
	 * @throws Exception 
	 * @throws Exception
	 */
	public void sendMail(String toMail,String subject,String text) throws Exception{
		Email email = new Email();
		email.setAddress(toMail);
		email.setSubject(subject);
		email.setContent(text);
		sendEmail(email, null);
	}

	/**
	 * 机构账号发送邮件
	 * 
	 * @param confInfo
	 *            邮箱配置信息
	 * @param msgContext
	 *            发送内容
	 * @param toMails
	 *            收件人邮箱
	 * @param emailTitle
	 *            邮件标题
	 * @throws Exception 
	 */
	public void sendByInstitutionEmail(LinksusRelationEmailConf confInfo,String toMail,String emailTitle,
			String msgContent) throws Exception{
		// 先配置邮件服务器
		MailProperty property = new MailProperty();
		property.setSmtpHost(confInfo.getSmtpHost());
		property.setUser(confInfo.getUserName());
		property.setPwd(confInfo.getPassword());
		property.setPort(confInfo.getPort());
		property.setFrom(confInfo.getEmailAddr());
		Email email = new Email();
		email.setAddress(toMail);
		email.setSubject(emailTitle);
		email.setContent(msgContent);
		sendEmail(email, property);
	}

	public void sendEmail(Email email,MailProperty property) throws Exception{
		JavaMailSenderImpl mailSender;
		String fromAddr = "";
		// 先配置邮件服务器
		if(property == null){
			mailSender = (JavaMailSenderImpl) sendMail;
			fromAddr = "services@1510cloud.com";
		}else{
			mailSender = new JavaMailSenderImpl();
			mailSender.setHost(property.getSmtpHost());
			mailSender.setUsername(property.getUser());
			mailSender.setPassword(property.getPwd());
			mailSender.setPort(property.getPort());
			fromAddr = property.getFrom();
		}
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.timeout", 25000);
		// 设置邮件的属性
		MimeMessage mime = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");
		if(property != null){
			// 设置回复到的账号
			if(!StringUtils.isBlank(property.getReplyTo())){
				helper.setReplyTo(property.getReplyTo());
			}
			// 使用安全连接
			if(property.getSslFlag() == 1){
				prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			}
		}
		mailSender.setJavaMailProperties(prop);
		// 设置发件人
		helper.setFrom(fromAddr);
		String address = email.getAddress();
		helper.setTo(address.split(","));// 收件人
		// helper.setBcc(email.getBcc());// 暗送
		String cc = email.getCc();
		if(!StringUtils.isBlank(cc)){
			helper.setCc(cc.split(","));// 抄送
		}
		// 内嵌资源，这种功能很少用，因为大部分资源都在网上，只需在邮件正文中给个URL就足够了.
		// helper.addInline(“logo”, new ClassPathResource(“logo.gif”)); 
		//处理附件
		if(email.getAnnexFile() != null){
			for(File file : email.getAnnexFile()){
				if(file == null){
					continue;
				}
				helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
			}
		}
		helper.setSubject(email.getSubject());// 邮件主题
		if(!email.isHtmlFlag()){
			helper.setText(email.getContent());
		}else{
			helper.setText(email.getContent(), true);// true表示设定html格式
		}
		mailSender.send(mime);
	}

}
