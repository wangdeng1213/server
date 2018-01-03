package com.linksus.ws.impl;

import java.util.Date;

import javax.jws.WebService;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.entity.LinksusGovRunning;
import com.linksus.entity.LinksusGovYixin;
import com.linksus.service.LinksusGovRunningService;
import com.linksus.service.LinksusGovYixinService;
import com.linksus.ws.YiXinReceiveService;

@WebService(endpointInterface = "com.linksus.ws.YiXinReceiveService")
public class YiXinReceiveServiceImpl implements YiXinReceiveService{

	LinksusGovYixinService yiXinService = (LinksusGovYixinService) ContextUtil.getBean("linksusGovYixinService");
	LinksusGovRunningService govRunningService = (LinksusGovRunningService) ContextUtil
			.getBean("linksusGovRunningService");

	@Override
	public String saveService(String serviceName,String xmlInfo){
		// 解析XML,然后将收到的信息保存到数据库中
		/**
		 * 消息类型包括：[text 文本，link 链接，image 图片，voice 语音，video 视频，news 图文]
		 *  <?xml version="1.0" encoding="UTF-8"?> 
		 *  	<BaseInfo>
		 *  		 <uid>用户id</uid>
		 * 			 <openid>易信用户openid</openid>
		 * 			 <type>类型</type>
		 * 			 <content>易信用户发送给第三方内容</content>
		 * 		 </BaseInfo>
		 */
		StringBuffer returnMsg = new StringBuffer();
		returnMsg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		returnMsg.append("<result>");
		try{
			Document doc = DocumentHelper.parseText(xmlInfo);
			Element root = doc.getRootElement();
			root.elementText("uid");
			String openid = root.elementText("openid");
			String type = root.elementText("type");
			String content = root.elementText("content");
			Long runId = PrimaryKeyGen.GenerateSerialNum();
			Integer gid = 0; //怎么通过opneId获得gid? 查询表linksus_gov_structure  where  appid=openid  ???
			Long accountId = 0L;
			Integer orgId = Constants.DEFAULTORG;//默认组织ID
			Long userId = 0L;//怎么通过uid获取userId
			//信息类型:1 文本消息 2 图片消息 3 语音 4 视频 5(回复)单图文信息 6(回复)多图文信息
			int msgType = 0;
			if(type.equals("text")){
				// 文本处理
				msgType = 1;
			}else if(type.equals("image")){
				// 图片处理
				msgType = 2;
			}else if(type.equals("link")){
				// 超链接处理

			}else if(type.equals("voice")){
				// 声音处理
				msgType = 3;
			}else if(type.equals("video")){
				// 视频处理
				msgType = 4;
			}else if(type.equals("news")){
				// 图文处理
				msgType = 5;
			}
			//QueueDataSave.addDataToQueue(saveGovRunning(runId,userId,gid,orgId), Constants.OPER_TYPE_INSERT);
			//QueueDataSave.addDataToQueue(saveGovYixin(runId,userId,accountId,openid,content,msgType), Constants.OPER_TYPE_INSERT);
			govRunningService.insertLinksusGovRunning(saveGovRunning(runId, userId, gid, orgId));
			yiXinService.insertLinksusGovYixin(saveGovYixin(runId, userId, accountId, openid, content, msgType));
			returnMsg.append("<ResultInfo>0</ResultInfo>");
		}catch (DocumentException e){
			returnMsg.append("<ResultInfo>-1</ResultInfo>");
			returnMsg.append("<Erro>消息格式错误</Erro>");
			returnMsg.append("<ErrorStack>").append(e.getMessage()).append("</ErrorStack>");
			e.printStackTrace();
		}catch (Exception e){
			returnMsg.append("<ResultInfo>-1</ResultInfo>");
			returnMsg.append("<Erro>服务端消息处理错误</Erro>");
			returnMsg.append("<ErrorStack>").append(e.getMessage()).append("</ErrorStack>");
		}
		returnMsg.append("</result>");
		return returnMsg.toString();
	}

	private LinksusGovRunning saveGovRunning(Long runId,Long userId,Integer gid,Integer orgId){
		LinksusGovRunning govRunning = new LinksusGovRunning();
		govRunning.setRunId(runId);
		govRunning.setGid(gid);
		govRunning.setCreateTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
		govRunning.setIsFinish(0);
		govRunning.setOrgId(orgId);
		//互动类型:1 评论 2 转发 3 @ 4 评论并@  5评论并转发 6 私信 7 平台账户回复
		govRunning.setInteractType(0);
		govRunning.setInteractMode(3);//互动方式： 1 微博 2 私信   3 易信
		govRunning.setUserId(userId);
		return govRunning;
	}

	private LinksusGovYixin saveGovYixin(Long runId,Long userId,Long accountId,String openid,String content,
			Integer msgType){
		LinksusGovYixin govYixin = new LinksusGovYixin();
		govYixin.setRunId(runId);
		govYixin.setAccountId(accountId);
		govYixin.setUserId(userId);
		govYixin.setOpenid(openid);
		govYixin.setContent(content);
		govYixin.setInteractType(2);
		govYixin.setMsgType(msgType);
		govYixin.setInteractTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
		return govYixin;
	}

}
