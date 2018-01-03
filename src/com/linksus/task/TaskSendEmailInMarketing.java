package com.linksus.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.MailUtil;
import com.linksus.entity.LinksusRelationEmailConf;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.entity.LinksusRelationPersonContact;
import com.linksus.service.LinksusInstitutionService;
import com.linksus.service.LinksusRelationEmailConfService;
import com.linksus.service.LinksusRelationMarketingItemService;
import com.linksus.service.LinksusRelationMarketingService;
import com.linksus.service.LinksusRelationPersonContactService;

public class TaskSendEmailInMarketing extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendEmailInMarketing.class);

	private String marketingType = "";

	LinksusRelationMarketingService linksusRelationMarketingService = (LinksusRelationMarketingService) ContextUtil
			.getBean("linksusRelationMarketingService");
	LinksusRelationMarketingItemService linksusRelationMarketingItemService = (LinksusRelationMarketingItemService) ContextUtil
			.getBean("linksusRelationMarketingItemService");
	LinksusInstitutionService linksusInstitutionService = (LinksusInstitutionService) ContextUtil
			.getBean("linksusInstitutionService");
	LinksusRelationEmailConfService linksusRelationEmailConfService = (LinksusRelationEmailConfService) ContextUtil
			.getBean("linksusRelationEmailConfService");
	LinksusRelationPersonContactService linksusRelationPersonContactService = (LinksusRelationPersonContactService) ContextUtil
			.getBean("linksusRelationPersonContactService");
	MailUtil mailUtil = (MailUtil) ContextUtil.getBean("mail");

	public static void main(String[] args){

		/*
		 * TaskSendEmailInMarketing dd = new TaskSendEmailInMarketing();
		 * dd.sendContentInMarketing("3"); System.exit(0);
		 */
	}

	public void setMarketingType(String marketingType){
		this.marketingType = marketingType;
	}

	@Override
	public void cal(){
		if(StringUtils.isNotBlank(marketingType)){
			sendContentInMarketing(marketingType);
		}
	}

	/**发布营销对象内容
	 * 
	 * marketingType :  4     执行邮件任务
	 * 
	 */
	public void sendContentInMarketing(String marketingType){
		//添加营销类型
		List<String> marketingTypes = new ArrayList<String>();
		marketingTypes.add(marketingType);
		Map paraMap = new HashMap();
		paraMap.put("marketingTypes", marketingTypes);
		paraMap.put("pageSize", pageSize);
		int startCount = (currentPage - 1) * pageSize;
		paraMap.put("startCount", startCount);
		List<LinksusRelationMarketing> marketings = linksusRelationMarketingService.getSendCommentsList(paraMap);
		if(marketings != null && marketings.size() > 0){
			for(int i = 0; i < marketings.size(); i++){
				LinksusRelationMarketing linksusRelationMarketing = marketings.get(i);
				if(linksusRelationMarketing != null){
					//获取营销列表——邮件
					try{
						sendEmailInMarketing(linksusRelationMarketing);
					}catch (Exception e){
						LogUtil.saveException(logger, e);
					}
				}
			}
		}
		checkTaskListEnd(marketings);
	}

	//营销类型为4——邮件
	public void sendEmailInMarketing(LinksusRelationMarketing linksusRelationMarketing){
		//获取当前基数
		int successCount = linksusRelationMarketing.getMarketingSuccessNum();
		int failCount = linksusRelationMarketing.getMarketingFailNum();

		long institutionId = linksusRelationMarketing.getInstitutionId();
		if(institutionId != 0){
			LinksusRelationEmailConf conf = linksusRelationEmailConfService
					.getLinksusRelationEmailConfInfo(institutionId);
			//获取营销对象
			List<LinksusRelationMarketingItem> items = linksusRelationMarketingItemService
					.getItemsByMarketingID(linksusRelationMarketing.getPid());
			if(items != null){
				for(int i = 0; i < items.size(); i++){
					LinksusRelationMarketingItem item = items.get(i);
					//通过人员联系方式表获取需要发邮件的邮箱列表
					List<LinksusRelationPersonContact> personContacts = linksusRelationPersonContactService
							.getPersonContactEmailListByPersonId(item.getUserId());
					if(personContacts != null && personContacts.size() != 0){
						for(int j = 0; j < personContacts.size(); j++){
							LinksusRelationPersonContact personContact = personContacts.get(j);
							try{
								//如果没有设置邮箱，通过系统邮箱发邮件
								if(conf != null){
									mailUtil.sendByInstitutionEmail(conf, personContact.getContact(),
											linksusRelationMarketing.getMarketingTitle(), linksusRelationMarketing
													.getMarketingContent());
								}else{
									MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail");
									mailUtils.sendMail(personContact.getContact(), linksusRelationMarketing
											.getMarketingTitle(), linksusRelationMarketing.getMarketingContent());
								}
								successCount++;
								//更新营销对象表执行状态
								item.setStatus(1);
								linksusRelationMarketingItemService.updateMarketingItemStatus(item);
							}catch (Exception e){
								failCount++;
								//更新营销对象表执行状态
								item.setStatus(2);
								linksusRelationMarketingItemService.updateMarketingItemStatus(item);
								LogUtil.saveException(logger, e);
								e.printStackTrace();
							}
						}
					}
				}
				//更新营销主表营销总数
				linksusRelationMarketing.setMarketingSuccessNum(successCount);
				linksusRelationMarketing.setMarketingFailNum(failCount);
				linksusRelationMarketingService.updateLinksysUserMarketing(linksusRelationMarketing);
			}
		}
	}

	//通过端口发送邮件
	public String sendSingleEmailContent(String id,String emailAdd,String emailTitle,String content,String isSystem){
		String errorCode = "";

		//isSystem为0时，通过机构发邮件，否则通过系统发
		if(isSystem.equals("0")){
			long institutionId = 0;
			try{
				institutionId = Long.parseLong(id);
			}catch (Exception e){
				errorCode = "10002";
				return errorCode;
			}
			if(institutionId != 0){
				LinksusRelationEmailConf conf = linksusRelationEmailConfService
						.getLinksusRelationEmailConfInfo(institutionId);
				if(conf != null){
					try{
						mailUtil.sendByInstitutionEmail(conf, emailAdd, emailTitle, content);
					}catch (Exception e){
						errorCode = "10009";
						return errorCode;
					}
				}else{
					errorCode = "10002";
					return errorCode;
				}
			}else{
				errorCode = "10002";
				return errorCode;
			}
		}else{
			try{
				mailUtil.sendMail(emailAdd, emailTitle, content);
			}catch (Exception e){
				errorCode = "10009";
				return errorCode;
			}
		}
		return errorCode;
	}

}
