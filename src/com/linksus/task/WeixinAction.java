package com.linksus.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.RelationUserAccountCommon;
import com.linksus.common.module.WeiboInteractCommon;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractAttentionReply;
import com.linksus.entity.LinksusInteractAttentionReplyAcct;
import com.linksus.entity.LinksusInteractKeywordReply;
import com.linksus.entity.LinksusInteractWeixin;
import com.linksus.entity.LinksusInteractWxArticle;
import com.linksus.entity.LinksusInteractWxMenuItem;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusRelationWxaccount;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.LinksusWeiXinReturnBean;
import com.linksus.entity.LinksusWeiXinUser;
import com.linksus.entity.LinksusWxMassGroup;
import com.linksus.entity.LinksusWxValidate;
import com.linksus.entity.UrlEntity;
import com.linksus.entity.WeiXinArticles;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractAttentionReplyAcctService;
import com.linksus.service.LinksusInteractAttentionReplyService;
import com.linksus.service.LinksusInteractKeywordReplyAcctService;
import com.linksus.service.LinksusInteractKeywordReplyService;
import com.linksus.service.LinksusInteractKeywordService;
import com.linksus.service.LinksusInteractWeixinService;
import com.linksus.service.LinksusInteractWxArticleService;
import com.linksus.service.LinksusInteractWxMenuAcctService;
import com.linksus.service.LinksusInteractWxMenuItemService;
import com.linksus.service.LinksusInteractWxMenuService;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationPersonService;
import com.linksus.service.LinksusRelationUserAccountService;
import com.linksus.service.LinksusRelationWxaccountService;
import com.linksus.service.LinksusRelationWxuserService;
import com.linksus.service.LinksusWxMassGroupService;
import com.linksus.service.LinksusWxService;
import com.linksus.service.LinksusWxValidateService;

/**
 * 客户端取得任务队列数据入口
 * 
 * @author zhangew
 * 
 */
@SuppressWarnings("serial")
public class WeixinAction extends HttpServlet{

	protected static final Logger logger = LoggerFactory.getLogger(WeixinAction.class);

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusRelationWxaccountService linksusRelationWxaccountService = (LinksusRelationWxaccountService) ContextUtil
			.getBean("linksusRelationWxaccountService");
	LinksusRelationPersonService linkPersonService = (LinksusRelationPersonService) ContextUtil
			.getBean("linksusRelationPersonService");
	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");
	LinksusRelationUserAccountService linksusRelationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");
	LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");
	LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");
	LinksusInteractWeixinService linksusInteractWeixinService = (LinksusInteractWeixinService) ContextUtil
			.getBean("linksusInteractWeixinService");
	LinksusInteractAttentionReplyAcctService linksusInteractAttentionReplyAcctService = (LinksusInteractAttentionReplyAcctService) ContextUtil
			.getBean("linksusInteractAttentionReplyAcctService");
	LinksusInteractAttentionReplyService linksusInteractAttentionReplyService = (LinksusInteractAttentionReplyService) ContextUtil
			.getBean("linksusInteractAttentionReplyService");
	LinksusInteractWxArticleService linksusInteractWxArticleService = (LinksusInteractWxArticleService) ContextUtil
			.getBean("linksusInteractWxArticleService");
	LinksusInteractKeywordReplyAcctService linksusInteractKeywordReplyAcctService = (LinksusInteractKeywordReplyAcctService) ContextUtil
			.getBean("linksusInteractKeywordReplyAcctService");
	LinksusInteractKeywordService linksusInteractKeywordService = (LinksusInteractKeywordService) ContextUtil
			.getBean("linksusInteractKeywordService");
	LinksusInteractWxMenuAcctService linksusInteractWxMenuAcctService = (LinksusInteractWxMenuAcctService) ContextUtil
			.getBean("linksusInteractWxMenuAcctService");
	LinksusInteractWxMenuService linksusInteractWxMenuService = (LinksusInteractWxMenuService) ContextUtil
			.getBean("linksusInteractWxMenuService");
	LinksusInteractKeywordReplyService linksusInteractKeywordReplyService = (LinksusInteractKeywordReplyService) ContextUtil
			.getBean("linksusInteractKeywordReplyService");
	LinksusInteractWxMenuItemService linksusInteractWxMenuItemService = (LinksusInteractWxMenuItemService) ContextUtil
			.getBean("linksusInteractWxMenuItemService");
	LinksusWxService linksusWxService = (LinksusWxService) ContextUtil.getBean("linksusWxService");
	LinksusWxMassGroupService massService = (LinksusWxMassGroupService) ContextUtil
			.getBean("linksusWxMassGroupService");

	CacheUtil cache = CacheUtil.getInstance();

	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		getAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		try{
			postAction(req, resp);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	//post调用接口
	private void postAction(HttpServletRequest req,HttpServletResponse response) throws Exception{
		String returnStr = "";
		//根据url中的微信号查询账号信息 vallid_token
		String uniqueCode = req.getParameter("uniqueCode");
		logger.info(">>>>>>>>>>>>>>>>>uniqueCode:{}", uniqueCode);
		uniqueCode = StringUtil.decodeBase64(uniqueCode);
		LinksusAppaccount appcount = linksusAppaccountService.getWxAppaccountByAccountName(uniqueCode);
		if(appcount == null){
			dealExceptionInvalidRecord(0l, 0l, new Exception("微信号:" + uniqueCode + ",未查询到有效账号!"));
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>未查询到有效账号");
			return;
		}
		//根据token判断信息的有效性
		if(!validateSignature(appcount.getValidToken(), req)){//验证失败
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>token校验失败 消息无效");
			//插入无效操作记录表
			dealExceptionInvalidRecord(appcount.getInstitutionId(), appcount.getId(), new Exception("token校验失败 消息无效!"));
			return;
		}

		response.setContentType("text/html;charset=UTF-8");
		ServletInputStream input = req.getInputStream();
		byte buffer[] = new byte[1024];
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while ((i = input.read(buffer)) != -1){
			buf.append(new String(buffer, 0, i));
		}
		LinksusWeiXinReturnBean returnBean = getWeiXinReturnBean(buf.toString());
		if(returnBean != null
				&& !(returnBean.getMsgType().equals("event") && returnBean.getEventType().equals("MASSSENDJOBFINISH"))){
			LinksusInteractWeixin interactWeixin = new LinksusInteractWeixin();
			String openid = returnBean.getFromUserName();
			long accountId = appcount.getId(); //平台账号ID
			long institutionId = appcount.getInstitutionId();

			long userId = 0; //微信用户ID
			long personId = 0; //人员ID
			//通过openid、appid查询userid
			if(StringUtils.isNotBlank(openid) && appcount.getWxType() == 0){//微信号为公众号时查询或读取用户信息
				Map params1 = new HashMap();
				params1.put("openid", openid);
				params1.put("appid", accountId);
				LinksusRelationWxaccount wxAccount = linksusRelationWxaccountService
						.getLinksusRelationWxaccountByPrimary(params1);
				if(wxAccount != null){//微信账号表中存在该账号
					userId = wxAccount.getUserId();
					//通过userid查询personId
					if(userId != 0){
						LinksusRelationWxuser relationWxuser = linksusRelationWxuserService
								.getLinksusRelationWxuserById(userId);
						if(relationWxuser != null){
							personId = relationWxuser.getPersonId();
						}
					}
				}else{//不存在此账号，抓取数据
					Map infos = addWeiXinUser(returnBean, appcount);
					if(infos != null && infos.size() > 0){
						personId = (Long) infos.get("personId");
						userId = (Long) infos.get("userId");
					}
				}
				if(userId != 0 && personId != 0){//微信号为公众号时
					interactWeixin.setUserId(userId);
					//处理接收事件推送
					if(returnBean.getMsgType().equals(Constants.WEIXINEVENTTYPE)){//关注/取消关注事件				
						if(returnBean.getEventType().equals(Constants.WEIXINEVENTSUB)){//关注消息
							//addWeiXinUser(returnBean,appcount);	
							//添加关注互动信息
							interactWeixin.setContent("关注事件");
							interactWeixin.setInteractType(3);
							interactWeixin.setMsgType(7);
							//插入互动信息表
							Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid,
									returnBean, interactWeixin);

							//关注自动回复
							if(accountId != 0){
								//查询关注自动回复账号关系表中是否有记录
								LinksusInteractAttentionReplyAcct replyAcct = linksusInteractAttentionReplyAcctService
										.getLinksusInteractAttentionReplyAcctByAccountId(accountId);
								if(replyAcct != null && replyAcct.getReplyId() != 0){
									//读取回复内容
									Map params = new HashMap();
									params.put("pid", replyAcct.getReplyId());
									params.put("replyType", "2");
									LinksusInteractAttentionReply attentionReply = linksusInteractAttentionReplyService
											.getLinksusInteractAttentionReplyByIdAndType(params);

									if(attentionReply != null){
										//回复
										Integer msgType = attentionReply.getMsgType();
										if(msgType == 2){
											msgType = 5;
										}else if(msgType == 3){
											msgType = 6;
										}
										String result = sendWeixinContent(accountId, appcount.getToken(), userId,
												attentionReply.getMaterialId(), msgType, attentionReply.getContent(),
												returnBean.getFromUserName());
										boolean flag = false;
										if(recordId != 0 && StringUtils.isNotBlank(result) && result.equals("sucess")){
											interactWeixin.setStatus(2);
										}else{
											flag = true;
											interactWeixin.setStatus(3);
										}
										interactWeixin.setInteractType(2);
										interactWeixin.setMaterialId(attentionReply.getMaterialId());
										interactWeixin.setContent(attentionReply.getContent());
										interactWeixin.setMsgType(7);
										Long pid = insertAccountIntoInteractWeixin(userId, personId, accountId,
												recordId, openid, returnBean, interactWeixin);
										if(StringUtils.isNotBlank(result) && flag && pid != 0){
											String errcode = "";
											if(result.equals("20035")){
												errcode = result;
												LinksusTaskErrorCode error = cache.getErrorCode(errcode);
												if(error != null){
													result = error.getErrorMsg();
												}
											}else{
												errcode = JsonUtil.getNodeValueByName(result, "errcode");
											}
											dealErrorCodeInvalidRecord(institutionId, pid, errcode, result);
										}
									}
								}
							}
						}else if(returnBean.getEventType().equals(Constants.WEIXINEVENTUNSUB)){//取消关注消息
							//添加取消关注互动信息
							interactWeixin.setContent("取消关注事件");
							interactWeixin.setInteractType(3);
							interactWeixin.setMsgType(8);
							//插入互动信息表
							Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid,
									returnBean, interactWeixin);
							deleteWeiXinrelationships(openid, accountId + "");
						}else if(returnBean.getEventType().equals(Constants.WEIXINEVENTCLICK)){//自定义菜单事件推送
							//添加菜单事件互动信息
							interactWeixin.setContent(returnBean.getEventKey());
							interactWeixin.setInteractType(3);
							interactWeixin.setMsgType(9);
							//插入互动信息表
							Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid,
									returnBean, interactWeixin);
							//获取自定义菜单EventKey
							if(StringUtils.isNotBlank(returnBean.getEventKey())){
								//根据eventKey回复
								LinksusInteractWxMenuItem wxMenuItem = linksusInteractWxMenuItemService
										.getLinksusInteractWxMenuItemById(Long.parseLong(returnBean.getEventKey()));
								if(wxMenuItem != null){
									//回复
									Integer msgType = wxMenuItem.getReplyType();
									if(msgType == 2){
										msgType = 5;
									}else if(msgType == 3){
										msgType = 6;
									}
									String result = sendWeixinContent(accountId, appcount.getToken(), userId,
											wxMenuItem.getMaterialId(), msgType, wxMenuItem.getContent(), returnBean
													.getFromUserName());
									boolean flag = false;
									if(recordId != 0 && StringUtils.isNotBlank(result) && result.equals("sucess")){
										interactWeixin.setStatus(2);
									}else{
										flag = true;
										interactWeixin.setStatus(3);
									}
									interactWeixin.setInteractType(5);
									interactWeixin.setMaterialId(wxMenuItem.getMaterialId());
									interactWeixin.setContent(wxMenuItem.getContent());
									interactWeixin.setMsgType(9);
									Long pid = insertAccountIntoInteractWeixin(userId, personId, accountId, recordId,
											openid, returnBean, interactWeixin);
									if(StringUtils.isNotBlank(result) && flag && pid != 0){
										String errcode = "";
										if(result.equals("20035")){
											errcode = result;
											LinksusTaskErrorCode error = cache.getErrorCode(errcode);
											if(error != null){
												result = error.getErrorMsg();
											}
										}else{
											errcode = JsonUtil.getNodeValueByName(result, "errcode");
										}
										dealErrorCodeInvalidRecord(institutionId, pid, errcode, result);
									}
								}else{
									logger.info("-------自定义菜单无回复信息-----");
								}
							}
						}
					}

					//处理接收普通消息
					if(returnBean.getMsgType().equals(Constants.WEIXINTEXTTYPE)){//文本消息
						interactWeixin.setContent(returnBean.getContent());
						interactWeixin.setMsgType(1);
						//添加用户互动信息
						interactWeixin.setInteractType(1);
						//插入互动信息表
						Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid, returnBean,
								interactWeixin);
						//关键字回复
						if(StringUtils.isNotBlank(returnBean.getContent())){
							String result = "";
							Map mapInfo = new HashMap();
							mapInfo.put("accountId", accountId);
							mapInfo.put("keywordName", returnBean.getContent());
							LinksusInteractKeywordReply keywordReply = linksusInteractKeywordReplyService
									.getAllKeywordsByAccountId(mapInfo);//匹配内容
							if(keywordReply != null){
								Integer msgType = keywordReply.getMsgType();
								if(msgType == 2){
									msgType = 5;
								}else if(msgType == 3){
									msgType = 6;
								}
								result = sendWeixinContent(accountId, appcount.getToken(), userId, keywordReply
										.getMaterialId(), msgType, keywordReply.getContent(), returnBean
										.getFromUserName());
								//关键字回复互动消息入库
								boolean flag = false;
								if(recordId != 0 && StringUtils.isNotBlank(result) && result.equals("sucess")){
									interactWeixin.setStatus(2);
								}else{
									flag = true;
									interactWeixin.setStatus(3);
								}
								interactWeixin.setInteractType(4);
								interactWeixin.setMaterialId(keywordReply.getMaterialId());
								interactWeixin.setContent(keywordReply.getContent());
								interactWeixin.setMsgType(keywordReply.getMsgType());
								Long pid = insertAccountIntoInteractWeixin(userId, personId, accountId, recordId,
										openid, returnBean, interactWeixin);
								if(StringUtils.isNotBlank(result) && flag && pid != 0){
									String errcode = "";
									if(result.equals("20035")){
										errcode = result;
										LinksusTaskErrorCode error = cache.getErrorCode(errcode);
										if(error != null){
											result = error.getErrorMsg();
										}
									}else{
										errcode = JsonUtil.getNodeValueByName(result, "errcode");
									}
									dealErrorCodeInvalidRecord(institutionId, pid, errcode, result);
								}
							}
						}
					}else if(returnBean.getMsgType().equals(Constants.WEIXINIMAGETYPE)){//图片消息
						//添加用户互动信息
						interactWeixin.setMsgType(2);
						String imagePath = getMediaPath(appcount.getToken(), returnBean.getMediaId(), "jpg",
								"image/jpeg");
						interactWeixin.setContent(imagePath);
						interactWeixin.setInteractType(1);
						//插入互动信息表
						Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid, returnBean,
								interactWeixin);
					}else if(returnBean.getMsgType().equals(Constants.WEIXINVOICETYPE)){//语音类型
						interactWeixin.setMsgType(3);
						String voicePath = getMediaPath(appcount.getToken(), returnBean.getMediaId(), "amr",
								"audio/amr");
						interactWeixin.setContent(voicePath);
						interactWeixin.setInteractType(1);
						//插入互动信息表
						Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid, returnBean,
								interactWeixin);
					}else if(returnBean.getMsgType().equals(Constants.WEIXINVIDEOTYPE)){ //视频类型
						interactWeixin.setMsgType(4);
						//视频文件不支持下载  video/mp4
						//String videoPath = getMediaPath(appcount.getToken(),returnBean.getMediaId(),returnBean.getFormat(),"video/x-pv-mp4");
						String thumbMediaIdPath = getMediaPath(appcount.getToken(), returnBean.getThumbMediaId(),
								"jpg", "image/jpeg");
						interactWeixin.setContent(thumbMediaIdPath);
						interactWeixin.setInteractType(1);
						//插入互动信息表
						Long recordId = insertUserIntoInteractWeixin(userId, personId, accountId, openid, returnBean,
								interactWeixin);
					}
				}else{
					logger.info("-------------无人员关联关系-----------------");
				}
			}else if(StringUtils.isNotBlank(openid) && appcount.getWxType() == 1){//微信号为订阅号时，不插入关系表，直接回复
				//处理接收事件推送
				if(returnBean.getMsgType().equals(Constants.WEIXINEVENTTYPE)){//关注/取消关注事件				
					if(returnBean.getEventType().equals(Constants.WEIXINEVENTSUB)){//关注消息

						//关注自动回复
						if(accountId != 0){
							//查询关注自动回复账号关系表中是否有记录
							LinksusInteractAttentionReplyAcct replyAcct = linksusInteractAttentionReplyAcctService
									.getLinksusInteractAttentionReplyAcctByAccountId(accountId);
							if(replyAcct != null && replyAcct.getReplyId() != 0){
								//读取回复内容
								Map params = new HashMap();
								params.put("pid", replyAcct.getReplyId());
								params.put("replyType", "2");
								LinksusInteractAttentionReply attentionReply = linksusInteractAttentionReplyService
										.getLinksusInteractAttentionReplyByIdAndType(params);
								if(attentionReply != null){
									//回复
									Integer msgType = attentionReply.getMsgType();
									if(msgType == 2){
										msgType = 5;
									}else if(msgType == 3){
										msgType = 6;
									}
									returnStr = getContentXML(attentionReply.getMaterialId(), msgType, attentionReply
											.getContent(), returnBean);
								}
							}
						}
					}else if(returnBean.getEventType().equals(Constants.WEIXINEVENTCLICK)){//自定义菜单事件推送
						//获取自定义菜单EventKey
						if(StringUtils.isNotBlank(returnBean.getEventKey())){
							//根据eventKey回复
							LinksusInteractWxMenuItem wxMenuItem = linksusInteractWxMenuItemService
									.getLinksusInteractWxMenuItemById(Long.parseLong(returnBean.getEventKey()));
							if(wxMenuItem != null){
								//回复
								Integer msgType = wxMenuItem.getReplyType();
								if(msgType == 2){
									msgType = 5;
								}else if(msgType == 3){
									msgType = 6;
								}
								returnStr = getContentXML(wxMenuItem.getMaterialId(), msgType, wxMenuItem.getContent(),
										returnBean);
							}else{
								logger.info("-------自定义菜单无回复信息-----");
							}
						}
					}
				}

				//处理接收普通消息
				if(returnBean.getMsgType().equals(Constants.WEIXINTEXTTYPE)){//文本消息
					//关键字回复
					if(StringUtils.isNotBlank(returnBean.getContent())){
						String result = "";
						Map mapInfo = new HashMap();
						mapInfo.put("accountId", accountId);
						mapInfo.put("keywordName", returnBean.getContent());
						LinksusInteractKeywordReply keywordReply = linksusInteractKeywordReplyService
								.getAllKeywordsByAccountId(mapInfo);//匹配内容
						if(keywordReply != null){
							Integer msgType = keywordReply.getMsgType();
							if(msgType == 2){
								msgType = 5;
							}else if(msgType == 3){
								msgType = 6;
							}
							returnStr = getContentXML(keywordReply.getMaterialId(), msgType, keywordReply.getContent(),
									returnBean);
						}
					}
				}
			}
		}else if(returnBean != null && returnBean.getMsgType().equals("event")
				&& returnBean.getEventType().equals("MASSSENDJOBFINISH")){
			//添加事件消息，修改群发结果
			//修改微信群发信息表结果
			LinksusWxMassGroup entity = new LinksusWxMassGroup();
			entity.setMsgId(new Long(returnBean.getMsgId()));
			entity.setAccountId(appcount.getId());
			String filterCount = returnBean.getFilterCount();
			if(StringUtils.isBlank(filterCount)){
				filterCount = "0";
			}
			entity.setFilterCount(new Integer(filterCount));
			String sentCount = returnBean.getSentCount();
			String errorCount = returnBean.getErrorCount();
			if(StringUtils.isBlank(sentCount)){
				sentCount = "0";
			}
			entity.setSentCount(new Integer(sentCount));
			if(StringUtils.isBlank(errorCount)){
				errorCount = "0";
			}
			entity.setErrorCount(new Integer(errorCount));
			entity.setStatusMsg(returnBean.getStatus());
			entity.setLastTime(DateUtil.getUnixDate(new Date()));
			massService.updateMassResultGroup(entity);

			//获取微信的发布信息主键
			Map mapInfo = new HashMap();
			mapInfo.put("msgId", new Long(returnBean.getMsgId()));
			mapInfo.put("accountId", appcount.getId());
			LinksusWxMassGroup massGroup = massService.getLinksusWxMassGroupByMsgId(mapInfo);
			//修改微信的发布信息返回结果
			if(massGroup != null){
				linksusWxService.updateWeiXinTaskErrmsgByGroup(massGroup.getWxId());
			}
		}else{
			response.getWriter().print("");
		}
		response.getWriter().print(returnStr);
	}

	/**
	 * 验证token
	 * @param token
	 * @param req
	 * @return
	 */
	private boolean validateSignature(String token,HttpServletRequest req){
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		List list = new ArrayList();
		list.add(token);
		list.add(timestamp);
		list.add(nonce);
		Collections.sort(list);
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < list.size(); i++){
			buffer.append(list.get(i));
		}
		String hex = DigestUtils.shaHex(buffer.toString());
		logger.info(">>>>>>>>>>>>>>>>>hex:{}", hex);
		if(signature.equals(hex)){
			logger.info(">>>>>>>>>>>>>>>>>true");
			return true;
		}else{
			LogUtil.saveException(logger, new Exception("微信URL校验失败!TOKEN不匹配!"));
			return false;
		}
	}

	/**
	 * 外部调用接口
	 * 
	 * @param req
	 * @param response
	 * @throws IOException
	 * @throws CacheException
	 */
	private void getAction(HttpServletRequest req,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		try{
			LinksusWxValidateService validateService = (LinksusWxValidateService) ContextUtil
					.getBean("linksusWxValidateService");
			String uniqueCode = req.getParameter("uniqueCode");//
			logger.info(">>>>>>>>>>>>>>>>>path:" + req.getRequestURL() + "?" + req.getQueryString());
			logger.info(">>>>>>>>>>>>>>>>>uniqueCode:" + uniqueCode);
			uniqueCode = StringUtil.decodeBase64(uniqueCode);
			LinksusWxValidate val = validateService.getLinksusWxValidateByWxnum(uniqueCode);
			if(val == null){
				LogUtil.saveException(logger, new Exception("微信URL绑定失败!平台未查询到相应用户!"));
				response.getWriter().print("error");
			}else{
				String token = val.getCode();
				if(validateSignature(token, req)){//绑定成功
					response.getWriter().print(req.getParameter("echostr"));
					val.setStatus(1);
					val.setValidTime(DateUtil.getUnixDate(new Date()));
					validateService.updateLinksusWxValidate(val);
				}
			}
			return;
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.saveException(logger, e);
			response.getWriter().print(StringUtil.getHttpResultForException(e));
		}
	}

	//将收取消息转换为Bean
	public LinksusWeiXinReturnBean getWeiXinReturnBean(String xmlContent){
		LinksusWeiXinReturnBean returnBean = null;
		try{
			Document document = DocumentHelper.parseText(xmlContent);
			Element rootElt = document.getRootElement(); // 获取根节点
			if(rootElt != null){
				returnBean = new LinksusWeiXinReturnBean();
				returnBean.setToUserName(rootElt.elementTextTrim("ToUserName"));
				returnBean.setFromUserName(rootElt.elementTextTrim("FromUserName"));
				returnBean.setCreateTime(rootElt.elementTextTrim("CreateTime"));
				String msgType = rootElt.elementTextTrim("MsgType");
				String msgId = "";
				if(msgType.equals("event")){//接收事件推送，排重使用FromUserName、CreateTime
					if(rootElt.elementTextTrim("Event").equals("MASSSENDJOBFINISH")){//群发事件消息与普通消息排重方法一致
						msgId = rootElt.elementTextTrim("MsgID");
					}else{
						msgId = rootElt.elementTextTrim("FromUserName") + rootElt.elementTextTrim("CreateTime");
					}
					if(StringUtils.isNotBlank(msgId) && RedisUtil.getRedisSet("weixin_msgid", msgId)){
						return null;
					}
				}else{//接收普通消息，排重使用MsgId
					msgId = rootElt.elementTextTrim("MsgId");
					if(StringUtils.isNotBlank(msgId) && RedisUtil.getRedisSet("weixin_msgid", msgId)){
						return null;
					}
				}
				if(StringUtils.isNotBlank(msgId)){//消息加入缓存
					RedisUtil.setRedisSet("weixin_msgid", msgId);
				}
				returnBean.setMsgType(msgType);
				if(msgType.equals(Constants.WEIXINTEXTTYPE)){//文本类型
					//String content = rootElt.elementTextTrim("Content");
					boolean flag = false;
					String content_temp = "此条信息微信接口暂不支持!";
					byte[] content = rootElt.elementTextTrim("Content").getBytes();
					for(int i = 0; i < content.length; i++){
						if((content[i] & 0xF8) == 0xF0){
							flag = true;
							break;
						}
					}
					returnBean.setContent(flag ? content_temp : new String(content));
					returnBean.setMsgId(rootElt.elementTextTrim("MsgId"));
				}else if(msgType.equals(Constants.WEIXINIMAGETYPE)){//图片类型
					returnBean.setMediaId(rootElt.elementTextTrim("MediaId"));
					returnBean.setMsgId(rootElt.elementTextTrim("MsgId"));
				}else if(msgType.equals(Constants.WEIXINVOICETYPE)){//语音类型
					returnBean.setMediaId(rootElt.elementTextTrim("MediaId"));
					returnBean.setMsgId(rootElt.elementTextTrim("MsgId"));
				}else if(msgType.equals(Constants.WEIXINVIDEOTYPE)){//视频类型
					returnBean.setMediaId(rootElt.elementTextTrim("MediaId"));
					returnBean.setThumbMediaId(rootElt.elementTextTrim("ThumbMediaId"));
					returnBean.setMsgId(rootElt.elementTextTrim("MsgId"));
				}else if(msgType.equals(Constants.WEIXINLOCATIONTYPE)){//地理位置类型

				}else if(msgType.equals(Constants.WEIXINLINKTYPE)){//链接类型

				}else if(msgType.equals(Constants.WEIXINEVENTTYPE)){//消息类型
					returnBean.setMsgType(msgType);
					returnBean.setEventType(rootElt.elementTextTrim("Event"));
					if(rootElt.elementTextTrim("Event").equals("CLICK")){
						returnBean.setEventKey(rootElt.elementTextTrim("EventKey"));
					}
					if(rootElt.elementTextTrim("Event").equals("MASSSENDJOBFINISH")){//群发事件	
						returnBean.setMsgId(rootElt.elementTextTrim("MsgID"));
						returnBean.setSentCount(rootElt.elementTextTrim("SentCount"));
						returnBean.setErrorCount(rootElt.elementTextTrim("ErrorCount"));
						returnBean.setFilterCount(rootElt.elementTextTrim("FilterCount"));
						returnBean.setStatus(rootElt.elementTextTrim("Status"));
					}
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
			return null;
		}
		return returnBean;
	}

	//加关注事件
	public Map addWeiXinUser(LinksusWeiXinReturnBean returnBean,LinksusAppaccount appAccount) throws Exception{
		//根据开发者微信号查询当前机构token
		Map map = new HashMap();
		if(appAccount != null){
			String token = appAccount.getToken();
			long institutionId = appAccount.getInstitutionId();
			if(StringUtils.isNotBlank(token)){
				LinksusWeiXinUser weixinUser = getWeiXinUserInfo(token, returnBean.getFromUserName());
				if(weixinUser != null){
					// TaskQueryWXUserFans queryWXUserFans = new TaskQueryWXUserFans();
					map = dealWeiXinUser(weixinUser, appAccount.getId());
				}else{
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<{}", "未返回微信粉丝信息");
				}
			}else{
				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<{}", "机构token错误");
			}
		}else{
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<{}", "无此机构");
		}
		return map;
	}

	//取消关注
	public void deleteWeiXinrelationships(String fromUserName,String accountId){
		//根据openId、appAccountId查询user_id
		Map params = new HashMap();
		params.put("openid", fromUserName);
		params.put("appid", accountId);
		LinksusRelationWxaccount wxAccount = linksusRelationWxaccountService
				.getLinksusRelationWxaccountByPrimary(params);
		if(wxAccount != null){
			long userId = wxAccount.getUserId();
			//删除人员关系表中此记录
			Map accountMap = new HashMap();
			accountMap.put("accountId", accountId);
			accountMap.put("accountType", 3);
			accountMap.put("userId", userId);
			linksusRelationUserAccountService.deleteLinksusRelationUserAccountByKey(accountMap);
		}
	}

	//读取微信粉丝信息
	public LinksusWeiXinUser getWeiXinUserInfo(String token,String openid){
		UrlEntity strUrl = LoadConfig.getUrlEntity("WeiXinUserInfo");
		Map parms = new HashMap();
		parms.put("access_token", token);
		parms.put("openid", openid);
		parms.put("lang", "zh_CN");
		String rsData = HttpUtil.getRequest(strUrl, parms);
		if(StringUtils.isNotBlank(rsData) && JsonUtil.getNodeByName(rsData, "errcode") == null){
			LinksusWeiXinUser weixinUser = (LinksusWeiXinUser) JsonUtil.json2Bean(rsData, LinksusWeiXinUser.class);
			if(weixinUser.getSubscribe() == 0){
				return null;
			}else{
				return weixinUser;
			}
		}else{
			return null;
		}
	}

	/**
	 * 1 open_id和公众号存在
	   1.1 md5相同 不更新/不处理
	   1.2 md5不相同 更新账号表/用户表
	   2 open_id和公众号不存在
	   2.1 md5相同 插入账号表,使用md5用户ID
	   2.2 md5不相同 新增账号表/用户表/人员表
	 */
	public Map dealWeiXinUser(LinksusWeiXinUser weixinUser,Long accountId) throws Exception{
		Map infoMap = new HashMap();
		if(weixinUser != null){
			String userSex = "n";
			if(weixinUser.getSex() != null){
				if(weixinUser.getSex().equals("1")){
					userSex = "m";
				}else if(weixinUser.getSex().equals("2")){
					userSex = "f";
				}
			}
			weixinUser.setSex(userSex);
			//加密用户nickname、sex 、city 、country 、province 、language 、headimgurl 
			StringBuffer buff = new StringBuffer(weixinUser.getNickname());
			buff.append(weixinUser.getSex());
			buff.append(weixinUser.getCity());
			buff.append(weixinUser.getCountry());
			buff.append(weixinUser.getProvince());
			buff.append(weixinUser.getLanguage());
			buff.append(weixinUser.getHeadimgurl());
			String msgMD5 = StringUtil.Md5To16Bit(buff.toString());
			if(StringUtils.isNotBlank(msgMD5)){
				//判断微信账号表中是否有重复
				LinksusRelationWxaccount wxAccountMd5 = linksusRelationWxaccountService
						.getLinksusRelationWxaccountByMsgMD5(msgMD5);
				Map map = new HashMap();
				map.put("openid", weixinUser.getOpenid());
				map.put("appid", accountId);
				LinksusRelationWxaccount wxAccountIds = linksusRelationWxaccountService
						.getLinksusRelationWxaccountByPrimary(map);
				if(wxAccountIds != null){
					//1 open_id和公众号存在
					if(wxAccountMd5 != null){
						//1.1 md5相同 不更新/不处理
						Long personId = linksusRelationWxuserService.getLinksusRelationWxuserInfo(
								wxAccountMd5.getUserId()).getPersonId();
						infoMap.put("personId", personId);
						infoMap.put("userId", wxAccountMd5.getUserId());
					}else{
						//1.2 md5不相同 更新账号表/用户表
						Long personId = linksusRelationWxuserService.getLinksusRelationWxuserInfo(
								wxAccountIds.getUserId()).getPersonId();
						LinksusRelationWxaccount wxAccount = genWxAccount(weixinUser, wxAccountIds.getUserId(),
								accountId, msgMD5);
						linksusRelationWxaccountService.updateLinksusRelationWxaccount(wxAccount);
						LinksusRelationWxuser wxUser = genWxUser(wxAccount, personId, wxAccountIds.getUserId());
						linksusRelationWxuserService.updateLinksusRelationWxuser(wxUser);
						infoMap.put("personId", personId);
						infoMap.put("userId", wxAccountIds.getUserId());
					}
				}else{
					//2 open_id和公众号不存在
					Long institutionId = linksusAppaccountService.getLinksusAppaccountById(accountId)
							.getInstitutionId();
					if(wxAccountMd5 != null){
						//2.1 md5相同 插入账号表/关系表,(user_id使用md5用户ID)
						Long personId = linksusRelationWxuserService.getLinksusRelationWxuserInfo(
								wxAccountMd5.getUserId()).getPersonId();
						LinksusRelationWxaccount newWXAccount = genWxAccount(weixinUser, wxAccountMd5.getUserId(),
								accountId, msgMD5);
						linksusRelationWxaccountService.insertLinksusRelationWxaccount(newWXAccount);
						LinksusRelationUserAccount userAccount = genUserAccount(wxAccountMd5.getUserId(), personId,
								accountId, institutionId);
						linksusRelationUserAccountService.insertWeiXineToLinksusRelationUserAccount(userAccount);
						//判断该机构在分组信息表中是否存在“未分组”
						RelationUserAccountCommon.dealPersonGroup(personId, institutionId, "2");
						infoMap.put("personId", personId);
						infoMap.put("userId", wxAccountMd5.getUserId());
					}else{
						//2.2 md5不相同 新增账号表/用户表/人员表/关系表
						Long personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
						Long userId = PrimaryKeyGen.getPrimaryKey("linksus_relation_wxuser", "user_id");
						LinksusRelationWxaccount wxAccount = genWxAccount(weixinUser, userId, accountId, msgMD5);
						linksusRelationWxaccountService.insertLinksusRelationWxaccount(wxAccount);
						LinksusRelationWxuser wxUser = genWxUser(wxAccount, personId, userId);
						linksusRelationWxuserService.insertLinksusRelationWxuser(wxUser);
						LinksusRelationPerson linkPerson = genWxPerson(wxUser, personId, userId);
						//linkPersonService.insertLinksusRelationPerson(linkPerson);
						QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
						//判断该机构在分组信息表中是否存在“未分组”
						RelationUserAccountCommon.dealPersonGroup(personId, institutionId, "2");
						infoMap.put("personId", personId);
						infoMap.put("userId", userId);
					}
				}
			}
		}
		return infoMap;
	}

	private LinksusRelationWxaccount genWxAccount(LinksusWeiXinUser weixinUser,Long userId,Long accountId,String msgMD5){
		LinksusRelationWxaccount newWXAccount = new LinksusRelationWxaccount();
		newWXAccount.setAppid(accountId);
		newWXAccount.setOpenid(weixinUser.getOpenid());
		newWXAccount.setUserId(userId);
		newWXAccount.setSex(weixinUser.getSex());
		newWXAccount.setNickname(weixinUser.getNickname());
		newWXAccount.setCity(weixinUser.getCity());
		newWXAccount.setCountry(weixinUser.getCountry());
		newWXAccount.setProvince(weixinUser.getProvince());
		newWXAccount.setHeadimgurl(weixinUser.getHeadimgurl());
		newWXAccount.setLanguage(weixinUser.getLanguage());
		newWXAccount.setMsgMd5(msgMD5);
		Integer subscribeTime = 0;
		if(weixinUser.getSubscribeTime() != null){
			subscribeTime = Integer.parseInt(weixinUser.getSubscribeTime());
		}
		newWXAccount.setSubscribeTime(subscribeTime);
		newWXAccount.setLastUpdtTime(DateUtil.getUnixDate(new Date()));
		return newWXAccount;
	}

	private LinksusRelationUserAccount genUserAccount(Long userId,Long personId,Long accountId,Long institutionId)
			throws Exception{
		Long pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_account", "pid");
		LinksusRelationUserAccount userAccount = new LinksusRelationUserAccount();
		userAccount.setPid(pid);
		userAccount.setAccountId(accountId);
		userAccount.setAccountType(3);
		userAccount.setInstitutionId(institutionId);
		userAccount.setFlagRelation(1);
		userAccount.setPersonId(personId);
		userAccount.setUserId(userId);
		userAccount.setFansTime(DateUtil.getUnixDate(new Date()));
		userAccount.setUptime(DateUtil.getUnixDate(new Date()));
		return userAccount;
	}

	private LinksusRelationWxuser genWxUser(LinksusRelationWxaccount wxaccount,Long personId,Long userId){
		LinksusRelationWxuser wxUser = new LinksusRelationWxuser();
		String country = wxaccount.getCountry();
		String countryCode = null;
		String province = wxaccount.getProvince();
		String provinceCode = null;
		String city = wxaccount.getCity();
		String cityCode = null;
		if(StringUtils.isNotBlank(country)){
			if(country.equals("中国香港")){
				country = "中国";
				countryCode = cache.getWeixinAreaCode("中国");
				province = "香港";
				provinceCode = cache.getWeixinAreaCode("香港");
				city = wxaccount.getProvince();
				cityCode = cache.getWeixinAreaCode(wxaccount.getProvince());
			}else if(country.equals("中国澳门")){
				country = "中国";
				countryCode = cache.getWeixinAreaCode("中国");
				province = "澳门";
				provinceCode = cache.getWeixinAreaCode("澳门");
				city = wxaccount.getProvince();
				cityCode = cache.getWeixinAreaCode(wxaccount.getProvince());
			}else if(country.equals("中国台湾")){
				country = "中国";
				countryCode = cache.getWeixinAreaCode("中国");
				province = "台湾";
				provinceCode = cache.getWeixinAreaCode("台湾");
				city = wxaccount.getProvince();
				cityCode = cache.getWeixinAreaCode(wxaccount.getProvince());
			}else{
				countryCode = cache.getWeixinAreaCode(country);
				provinceCode = cache.getWeixinAreaCode(province);
				cityCode = cache.getWeixinAreaCode(wxaccount.getCity());
			}
		}
		if(StringUtils.isNotBlank(countryCode)){
			wxUser.setCountry(country);
			wxUser.setCountryCode(countryCode);
		}else{
			wxUser.setCountry("");
			wxUser.setCountryCode("");
		}
		if(StringUtils.isNotBlank(provinceCode)){
			wxUser.setProvince(province);
			wxUser.setProvinceCode(provinceCode);
		}else{
			wxUser.setProvince("");
			wxUser.setProvinceCode("");
		}
		if(StringUtils.isNotBlank(cityCode)){
			wxUser.setCityCode(cityCode);
		}else{
			wxUser.setCityCode("");
		}
		wxUser.setCity(city);
		wxUser.setCityCode(cityCode);
		wxUser.setCountry(country);
		wxUser.setCountryCode(countryCode);
		wxUser.setProvince(province);
		wxUser.setProvinceCode(provinceCode);
		wxUser.setUserId(userId);
		wxUser.setPersonId(personId);
		wxUser.setHeadimgurl(wxaccount.getHeadimgurl());
		wxUser.setNickname(wxaccount.getNickname());
		wxUser.setSex(wxaccount.getSex());
		wxUser.setLanguage(wxaccount.getLanguage());
		wxUser.setLastUpdtTime(DateUtil.getUnixDate(new Date()));
		return wxUser;
	}

	private LinksusRelationPerson genWxPerson(LinksusRelationWxuser wxUser,Long personId,Long userId){
		LinksusRelationPerson linkPerson = new LinksusRelationPerson();
		linkPerson.setPersonName(wxUser.getNickname());
		linkPerson.setGender(wxUser.getSex());
		linkPerson.setBirthDay("");
		linkPerson.setCountryCode(wxUser.getCountryCode());
		linkPerson.setStateCode(wxUser.getProvinceCode());
		linkPerson.setCityCode(wxUser.getCity());
		String location = wxUser.getCountry() + " " + wxUser.getProvince() + " " + wxUser.getCity();
		linkPerson.setLocation(location.trim());
		linkPerson.setHeadimgurl(wxUser.getHeadimgurl());
		linkPerson.setPersonId(personId);
		linkPerson.setWeixinIds(String.valueOf(userId));
		linkPerson.setSinaIds("");
		linkPerson.setTencentIds("");
		linkPerson.setAddTime(DateUtil.getUnixDate(new Date()));
		linkPerson.setSynctime(DateUtil.getUnixDate(new Date()));
		return linkPerson;
	}

	//获取多媒体内容上传图片服务器mediaId
	public String getMediaPath(String token,String mediaId,String fileType,String ContentType){
		//String fileType="png";
		Map params = new HashMap();
		params.put("access_token", token);
		params.put("media_id", mediaId);
		byte[] buff = HttpUtil.getByteRequest(LoadConfig.getUrlEntity("getwxUserMedia"), params, ContentType);
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		Map paramMap = new HashMap();
		String resultStr = "";
		if(ContentType.equals("image/jpeg")){
			resultStr = "original_" + fileType;
			paramMap.put("original." + fileType, encoder.encode(buff));
		}else{
			resultStr = "media_" + fileType;
			paramMap.put("media." + fileType, encoder.encode(buff));
		}
		String imagePath = HttpUtil.sendToDataServer(paramMap);
		if(StringUtils.isNotBlank(imagePath)){
			imagePath = JsonUtil.getNodeByName(imagePath, "data");
			if(StringUtils.isNotBlank(imagePath)){
				imagePath = JsonUtil.getNodeValueByName(imagePath, resultStr);
			}
		}
		if(StringUtils.isBlank(imagePath)){
			imagePath = "";
		}
		return imagePath;
	}

	//发布微信内容
	public String sendWeixinContent(Long accountId,String token,Long userId,long materialId,Integer msgType,
			String content,String toUser){
		String strResult = null;
		//查看账户用户是否为关注状态
		LinksusRelationUserAccount userAccount = new LinksusRelationUserAccount();
		userAccount.setAccountType(3);
		userAccount.setAccountId(accountId);
		userAccount.setUserId(userId);
		linksusRelationUserAccountService.getLinksusWeiboRelation(userAccount);
		if(userAccount != null){
			boolean isFlag = false;
			List<LinksusInteractWxArticle> wxArticles = null;
			Map map1 = new HashMap();
			Map map = new HashMap();
			map.put("touser", toUser); // 测试
			//通过material_id获取微信图文表内容
			if(materialId != 0){
				wxArticles = linksusInteractWxArticleService.getLinksusInteractWxArticleByMaterialId(materialId);
			}
			if(msgType == 1){//回复纯文本				
				map1.put("content", content);//文本内容	
				map.put("msgtype", "text");
				map.put("text", map1);
				isFlag = true;
			}else if(msgType == 5){//单图文
				if(wxArticles != null && wxArticles.size() > 0){
					isFlag = true;
					LinksusInteractWxArticle interactWxArticle = wxArticles.get(0);
					map.put("msgtype", "news");
					List<WeiXinArticles> contents = new ArrayList<WeiXinArticles>();
					WeiXinArticles weixin = new WeiXinArticles();
					weixin.setTitle(interactWxArticle.getTitle());
					weixin.setDescription(interactWxArticle.getSummary());
					weixin.setUrl(interactWxArticle.getConentUrl());
					weixin.setPicurl(interactWxArticle.getPicOriginalUrl());
					contents.add(weixin);
					map1.put("articles", contents);
					map.put("news", map1);
				}
			}else if(msgType == 6){//多图文
				if(wxArticles != null && wxArticles.size() > 0){
					isFlag = true;
					map.put("msgtype", "news");
					List<WeiXinArticles> contents = new ArrayList<WeiXinArticles>();
					for(LinksusInteractWxArticle interactWxArticle : wxArticles){
						WeiXinArticles weixin = new WeiXinArticles();
						weixin.setTitle(interactWxArticle.getTitle());
						weixin.setDescription(interactWxArticle.getSummary());
						weixin.setUrl(interactWxArticle.getConentUrl());
						weixin.setPicurl(interactWxArticle.getPicOriginalUrl());
						contents.add(weixin);
					}
					map1.put("articles", contents);
					map.put("news", map1);
				}
			}

			if(isFlag){
				//发送微信
				strResult = HttpUtil.postBodyRequest(LoadConfig.getUrlEntity("sendCustomerServiceInfo"),
						"access_token=" + token, JsonUtil.map2json(map));
				if(StringUtils.isNotBlank(strResult) && JsonUtil.getNodeValueByName(strResult, "errcode").equals("0")){
					strResult = "sucess";
				}else{
					if(Constants.WECHATNOTEXISTENT.equals(JsonUtil.getNodeValueByName(strResult, "errcode"))){
						strResult = "20035";
					}
				}
				//Integer currentTime = Integer.parseInt(String.valueOf(new Date().getTime()/1000));
			}
		}else{
			strResult = "20035";
		}
		return strResult;
	}

	//插入用户微信互动信息表
	public Long insertUserIntoInteractWeixin(Long userId,Long personId,Long accountId,String openid,
			LinksusWeiXinReturnBean returnBean,LinksusInteractWeixin interactWeixin) throws Exception{
		Integer count = 0;
		WeiboInteractCommon interactCommon = new WeiboInteractCommon();
		long pId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weixin", "pid"); //获取主键					
		//获取互动记录ID 
		long recordId = interactCommon.dealWeiboInteract(userId, personId, accountId, Integer.valueOf(3), new Long(0),
				"6", pId, Integer.parseInt(returnBean.getCreateTime()));
		//将互动内容插入微信互动信息表中
		if(recordId != 0){
			//用户互动信息入库
			interactWeixin.setPid(pId);
			interactWeixin.setRecordId(recordId);
			interactWeixin.setUserId(userId);
			interactWeixin.setAccountId(accountId);
			interactWeixin.setOpenid(openid);
			interactWeixin.setMaterialId(0L);
			interactWeixin.setSendType(0);
			interactWeixin.setSendTime(0);
			interactWeixin.setStatus(0);
			interactWeixin.setInstPersonId(0L);
			interactWeixin.setInteractTime(Integer.parseInt(returnBean.getCreateTime()));
			count = linksusInteractWeixinService.insertLinksusInteractWeixin(interactWeixin);
		}
		if(count == 0){
			recordId = 0L;
		}
		return recordId;
	}

	//插入账户微信互动信息表
	public Long insertAccountIntoInteractWeixin(Long userId,Long personId,Long accountId,Long recordId,String openid,
			LinksusWeiXinReturnBean returnBean,LinksusInteractWeixin interactWeixin) throws Exception{
		Long pId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weixin", "pid"); //获取主键								
		//将互动内容插入微信互动信息表中
		if(recordId != 0){
			//用户互动信息入库
			interactWeixin.setPid(pId);
			interactWeixin.setRecordId(recordId);
			interactWeixin.setUserId(userId);
			interactWeixin.setAccountId(accountId);
			interactWeixin.setOpenid(openid);
			//interactWeixin.setInteractType(1);
			//interactWeixin.setMaterialId(0L);
			//interactWeixin.setStatus(0);
			interactWeixin.setSendType(0);
			interactWeixin.setSendTime(0);
			interactWeixin.setInstPersonId(interactWeixin.getInstPersonId());
			interactWeixin.setInteractTime(Integer.parseInt(returnBean.getCreateTime()));
			linksusInteractWeixinService.insertLinksusInteractWeixin(interactWeixin);
		}
		return pId;
	}

	//发送被动响应消息
	public String getContentXML(long materialId,Integer msgType,String content,LinksusWeiXinReturnBean returnBean){
		String resultStr = "";
		List<LinksusInteractWxArticle> wxArticles = null;
		//通过material_id获取微信图文表内容
		if(materialId != 0){
			wxArticles = linksusInteractWxArticleService.getLinksusInteractWxArticleByMaterialId(materialId);
		}
		boolean isFlag = false;
		Map map = new HashMap();
		map.put("ToUserName", returnBean.getFromUserName()); // 测试
		map.put("FromUserName", returnBean.getToUserName());
		map.put("CreateTime", returnBean.getCreateTime());
		if(msgType == 1){//回复纯文本	
			map.put("MsgType", "text");
			map.put("Content", content);//文本内容	
			resultStr = JsonUtil.mapTextToXML(map);
			isFlag = true;
		}else if(msgType == 5){//单图文
			if(wxArticles != null && wxArticles.size() > 0){
				isFlag = true;
				LinksusInteractWxArticle interactWxArticle = wxArticles.get(0);
				map.put("MsgType", "news");
				map.put("ArticleCount", wxArticles.size());
				Map map1 = new HashMap();
				map1.put("Title", interactWxArticle.getTitle());
				map1.put("Description", interactWxArticle.getSummary());
				map1.put("PicUrl", interactWxArticle.getPicOriginalUrl());
				map1.put("Url", interactWxArticle.getConentUrl());
				resultStr = "<xml>";
				resultStr = resultStr + JsonUtil.mapImageToXML(map);
				List imageXml = new ArrayList<Map<?, ?>>();
				imageXml.add(map1);
				resultStr = resultStr + JsonUtil.mapImageContentToXML(imageXml);
				resultStr = resultStr + "</xml>";
			}
		}else if(msgType == 6){//多图文
			if(wxArticles != null && wxArticles.size() > 0){
				isFlag = true;
				map.put("MsgType", "news");
				//List<WeiXinArticles> contents = new ArrayList<WeiXinArticles>();
				List imageXml = new ArrayList<Map<?, ?>>();
				for(LinksusInteractWxArticle interactWxArticle : wxArticles){
					Map map1 = new HashMap();
					map1.put("Title", interactWxArticle.getTitle());
					map1.put("Description", interactWxArticle.getSummary());
					map1.put("PicUrl", interactWxArticle.getPicOriginalUrl());
					map1.put("Url", interactWxArticle.getConentUrl());
					imageXml.add(map1);
				}
				resultStr = "<xml>";
				resultStr = resultStr + JsonUtil.mapImageToXML(map);
				resultStr = resultStr + JsonUtil.mapImageContentToXML(imageXml);
				resultStr = resultStr + "</xml>";
			}
		}
		return resultStr;
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_INFO);
			invalidRecord.setRecordId(pid);
			BaseTask.dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private void dealErrorCodeInvalidRecord(Long institutionId,Long pid,String errcode,String rsData){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(errcode);
			invalidRecord.setErrorMsg(rsData);
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIXIN);
			invalidRecord.setRecordId(pid);
			BaseTask.dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException{
		String content = "<xml><ToUserName><![CDATA[gh_b8fee7142314]]></ToUserName>"
				+ "<FromUserName><![CDATA[oSamXjmKr-hFXibYxJMvage4gv8w]]></FromUserName>"
				+ "<CreateTime>1398165180</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[]]></Content>" + "<MsgId>6005073722706482441</MsgId>" + "</xml>";
		new WeixinAction().getWeiXinReturnBean(content);
	}
}
