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
		// ����XML,Ȼ���յ�����Ϣ���浽���ݿ���
		/**
		 * ��Ϣ���Ͱ�����[text �ı���link ���ӣ�image ͼƬ��voice ������video ��Ƶ��news ͼ��]
		 *  <?xml version="1.0" encoding="UTF-8"?> 
		 *  	<BaseInfo>
		 *  		 <uid>�û�id</uid>
		 * 			 <openid>�����û�openid</openid>
		 * 			 <type>����</type>
		 * 			 <content>�����û����͸�����������</content>
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
			Integer gid = 0; //��ôͨ��opneId���gid? ��ѯ��linksus_gov_structure  where  appid=openid  ???
			Long accountId = 0L;
			Integer orgId = Constants.DEFAULTORG;//Ĭ����֯ID
			Long userId = 0L;//��ôͨ��uid��ȡuserId
			//��Ϣ����:1 �ı���Ϣ 2 ͼƬ��Ϣ 3 ���� 4 ��Ƶ 5(�ظ�)��ͼ����Ϣ 6(�ظ�)��ͼ����Ϣ
			int msgType = 0;
			if(type.equals("text")){
				// �ı�����
				msgType = 1;
			}else if(type.equals("image")){
				// ͼƬ����
				msgType = 2;
			}else if(type.equals("link")){
				// �����Ӵ���

			}else if(type.equals("voice")){
				// ��������
				msgType = 3;
			}else if(type.equals("video")){
				// ��Ƶ����
				msgType = 4;
			}else if(type.equals("news")){
				// ͼ�Ĵ���
				msgType = 5;
			}
			//QueueDataSave.addDataToQueue(saveGovRunning(runId,userId,gid,orgId), Constants.OPER_TYPE_INSERT);
			//QueueDataSave.addDataToQueue(saveGovYixin(runId,userId,accountId,openid,content,msgType), Constants.OPER_TYPE_INSERT);
			govRunningService.insertLinksusGovRunning(saveGovRunning(runId, userId, gid, orgId));
			yiXinService.insertLinksusGovYixin(saveGovYixin(runId, userId, accountId, openid, content, msgType));
			returnMsg.append("<ResultInfo>0</ResultInfo>");
		}catch (DocumentException e){
			returnMsg.append("<ResultInfo>-1</ResultInfo>");
			returnMsg.append("<Erro>��Ϣ��ʽ����</Erro>");
			returnMsg.append("<ErrorStack>").append(e.getMessage()).append("</ErrorStack>");
			e.printStackTrace();
		}catch (Exception e){
			returnMsg.append("<ResultInfo>-1</ResultInfo>");
			returnMsg.append("<Erro>�������Ϣ�������</Erro>");
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
		//��������:1 ���� 2 ת�� 3 @ 4 ���۲�@  5���۲�ת�� 6 ˽�� 7 ƽ̨�˻��ظ�
		govRunning.setInteractType(0);
		govRunning.setInteractMode(3);//������ʽ�� 1 ΢�� 2 ˽��   3 ����
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
