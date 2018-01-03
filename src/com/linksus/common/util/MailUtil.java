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
 * email������
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
	 * �����ʼ�
	 * 
	 * @param toMails
	 *            �ռ���
	 * @param subject
	 *            �ʼ�����
	 * @param text
	 *            �ʼ�����
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
	 * �����˺ŷ����ʼ�
	 * 
	 * @param confInfo
	 *            ����������Ϣ
	 * @param msgContext
	 *            ��������
	 * @param toMails
	 *            �ռ�������
	 * @param emailTitle
	 *            �ʼ�����
	 * @throws Exception 
	 */
	public void sendByInstitutionEmail(LinksusRelationEmailConf confInfo,String toMail,String emailTitle,
			String msgContent) throws Exception{
		// �������ʼ�������
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
		// �������ʼ�������
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
		// �����ʼ�������
		MimeMessage mime = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");
		if(property != null){
			// ���ûظ������˺�
			if(!StringUtils.isBlank(property.getReplyTo())){
				helper.setReplyTo(property.getReplyTo());
			}
			// ʹ�ð�ȫ����
			if(property.getSslFlag() == 1){
				prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			}
		}
		mailSender.setJavaMailProperties(prop);
		// ���÷�����
		helper.setFrom(fromAddr);
		String address = email.getAddress();
		helper.setTo(address.split(","));// �ռ���
		// helper.setBcc(email.getBcc());// ����
		String cc = email.getCc();
		if(!StringUtils.isBlank(cc)){
			helper.setCc(cc.split(","));// ����
		}
		// ��Ƕ��Դ�����ֹ��ܺ����ã���Ϊ�󲿷���Դ�������ϣ�ֻ�����ʼ������и���URL���㹻��.
		// helper.addInline(��logo��, new ClassPathResource(��logo.gif��)); 
		//������
		if(email.getAnnexFile() != null){
			for(File file : email.getAnnexFile()){
				if(file == null){
					continue;
				}
				helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
			}
		}
		helper.setSubject(email.getSubject());// �ʼ�����
		if(!email.isHtmlFlag()){
			helper.setText(email.getContent());
		}else{
			helper.setText(email.getContent(), true);// true��ʾ�趨html��ʽ
		}
		mailSender.send(mime);
	}

}
