package com.linksus.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.SmsUtil;
import com.linksus.entity.LinksusInstitution;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.entity.LinksusRelationPersonContact;
import com.linksus.service.LinksusInstitutionService;
import com.linksus.service.LinksusRelationMarketingItemService;
import com.linksus.service.LinksusRelationMarketingService;
import com.linksus.service.LinksusRelationPersonContactService;

/**
 * 发送短信任务
 */
public class TaskSendSmsInMarketing extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendSmsInMarketing.class);

	private String marketingType = "";

	LinksusRelationMarketingService linksusRelationMarketingService = (LinksusRelationMarketingService) ContextUtil
			.getBean("linksusRelationMarketingService");
	LinksusRelationMarketingItemService linksusRelationMarketingItemService = (LinksusRelationMarketingItemService) ContextUtil
			.getBean("linksusRelationMarketingItemService");
	LinksusInstitutionService linksusInstitutionService = (LinksusInstitutionService) ContextUtil
			.getBean("linksusInstitutionService");
	LinksusRelationPersonContactService linksusRelationPersonContactService = (LinksusRelationPersonContactService) ContextUtil
			.getBean("linksusRelationPersonContactService");

	public static void main(String[] args) throws UnsupportedEncodingException{
		//String s="%A8%A6%3F%3F%A8%A8%3F%3F%3F%3F%3F%3F%3F%3FSD5FJ2";
		String s = "%E9%AA%8C%E8%AF%81%E7%A0%81%EF%BC%9AEK8IQT";
		URLDecoder.decode(s, "UTF-8");
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
	 * marketingType : 3     执行短信任务
	 * 
	 */
	public void sendContentInMarketing(String marketingType){
		//获取营销列表——发送短信列表
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
					try{
						//通过营销主表关联营销对象表，发送短信
						sendInstitutionInMarketing(linksusRelationMarketing);
					}catch (Exception e){
						LogUtil.saveException(logger, e);
					}

				}
			}
		}
		checkTaskListEnd(marketings);
	}

	//营销类型为3——短信  如何通过user_id获取用户手机号
	public void sendInstitutionInMarketing(LinksusRelationMarketing linksusRelationMarketing){
		//获取当前基数
		int successCount = linksusRelationMarketing.getMarketingSuccessNum();
		int failCount = linksusRelationMarketing.getMarketingFailNum();

		//获取机构ID
		long institutionId = linksusRelationMarketing.getInstitutionId();
		if(institutionId != 0){
			//查询该机构是否有短信剩余
			int msgCount = 0;
			LinksusInstitution linksusInstitution = linksusInstitutionService.getSmsNumber(institutionId);
			if(linksusInstitution != null){
				msgCount = linksusInstitution.getSmsNumber();
			}
			//获取营销对象手机号
			List<LinksusRelationMarketingItem> items = linksusRelationMarketingItemService
					.getItemsByMarketingID(linksusRelationMarketing.getPid());
			if(items != null){
				for(int i = 0; i < items.size(); i++){
					LinksusRelationMarketingItem item = items.get(i);
					//通过人员联系方式表获取需要发短信的手机号
					List<LinksusRelationPersonContact> personContacts = linksusRelationPersonContactService
							.getPersonContactMobileListByPersonId(item.getUserId());
					if(personContacts != null && personContacts.size() != 0){
						for(int j = 0; j < personContacts.size(); j++){
							LinksusRelationPersonContact personContact = personContacts.get(j);
							//短信有剩余时，开始发短信
							if(msgCount > 0){
								try{
									//发短信
									SmsUtil.sendSms(personContact.getContact().toString(), linksusRelationMarketing
											.getMarketingContent());
									//短信余量减1
									linksusInstitution.setSmsNumber(msgCount - 1);
									linksusInstitutionService.minusSmsNumber(linksusInstitution);
									successCount++;
									//更新营销对象表执行状态
									item.setStatus(1);
									linksusRelationMarketingItemService.updateMarketingItemStatus(item);
								}catch (Exception e){
									failCount++;
									//更新营销对象表执行状态
									item.setStatus(2);
									linksusRelationMarketingItemService.updateMarketingItemStatus(item);
								}
								msgCount--;
							}else{
								//余额不足
								failCount++;
								//更新营销对象表执行状态
								item.setStatus(2);
								linksusRelationMarketingItemService.updateMarketingItemStatus(item);
							}
						}
					}else{
						failCount++;
						//更新营销对象表执行状态
						item.setStatus(2);
						linksusRelationMarketingItemService.updateMarketingItemStatus(item);
					}
				}
				//更新营销主表营销总数
				linksusRelationMarketing.setMarketingSuccessNum(successCount);
				linksusRelationMarketing.setMarketingFailNum(failCount);
				linksusRelationMarketingService.updateLinksysUserMarketing(linksusRelationMarketing);
			}
		}
	}

	//通过端口发送短信
	public String sendSinglePhoneContent(String id,String num,String content,String isSystem){
		String errorCode = "";

		//isSystem为0时，通过机构发短信，否则通过系统发
		if(isSystem.equals("0")){
			long institutionId = 0;
			int msgCount = 0;
			try{
				institutionId = Long.parseLong(id);
			}catch (Exception e){
				errorCode = "10002";
				return errorCode;
			}
			if(institutionId != 0){
				LinksusInstitution linksusInstitution = linksusInstitutionService.getSmsNumber(institutionId);
				if(linksusInstitution != null){
					msgCount = linksusInstitution.getSmsNumber();
					if(msgCount > 0){
						//通过机构发短信
						try{
							SmsUtil.sendSms(num + "", content);
							//短信余量减1
							linksusInstitution.setSmsNumber(msgCount - 1);
							linksusInstitutionService.minusSmsNumber(linksusInstitution);
						}catch (Exception e){
							errorCode = "10008";
						}
					}else{
						errorCode = "10010";
						return errorCode;
					}
				}
			}else{
				errorCode = "10002";
				return errorCode;
			}
		}else{
			//通过系统发短信
			try{
				SmsUtil.sendSms(num + "", content);
			}catch (Exception e){
				errorCode = "10008";
			}
		}
		return errorCode;
	}

}
