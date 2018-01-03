package com.linksus.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.entity.InteractListQueryObj;
import com.linksus.entity.InteractListResultObj;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusInteractPersonService;
import com.linksus.service.LinksusInteractUserService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusInteractWeixinService;

public class TaskInteractListInterface extends BaseInterface{

	LinksusInteractPersonService personService = (LinksusInteractPersonService) ContextUtil
			.getBean("linksusInteractPersonService");
	LinksusInteractMessageService msgService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusInteractUserService userService = (LinksusInteractUserService) ContextUtil
			.getBean("linksusInteractUserService");
	LinksusInteractWeiboService weiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");
	LinksusInteractWeixinService weixinService = (LinksusInteractWeixinService) ContextUtil
			.getBean("linksusInteractWeixinService");

	// 互动列表页面
	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		try{
			String paramJson = (String) paramsMap.get("paramJson");
			//String paramJson="{\"page\":1,\"accountIds\":[180,2],\"status\":null,\"instPersonId\":0,\"rpsGenders\":null,\"countryCode\":null,\"rpsProvince\":\"2\",\"rpsCity\":\"3\",\"institutionId\":1013,\"pageSize\":15,\"startCount\":0,\"totalCount\":-1,\"sort\":\"\",\"sortType\":\"3\"}";
			logger.debug(">>>>>>>>>>>>>>>>>>>paramJson={}", paramJson);
			InteractListQueryObj obj = (InteractListQueryObj) JsonUtil.json2Bean(paramJson, InteractListQueryObj.class);
			//1:根据分配状态,排序,筛选条件,分页 查询符合条件的person 查询未处理总数,最近互动时间,最近互动记录id 
			logger.debug(">>>>>>>>>>>>>>>>>>>paramJson={}", paramJson);
			Integer page = obj.getPage();
			obj.setStartCount((page - 1) * obj.getPageSize());
			//处理地域
			if(!StringUtils.isBlank(obj.getRpsCity())){
				obj.setRpsProvince(null);
				obj.setCountryCode(null);
			}
			if(!StringUtils.isBlank(obj.getRpsProvince())){
				obj.setCountryCode(null);
			}
			//处理认证类型
			List<Integer> list = obj.getVerifiedTypeFlags();
			if(list != null && list.size() > 0){
				List sinaType = new ArrayList();
				List tencentType = new ArrayList();
				for(int i = 0; i < list.size(); i++){
					Integer type = list.get(i);
					if(type.intValue() == 1 || type.intValue() == 2 || type.intValue() == 3){
						sinaType.add(type);
					}else if(type.intValue() == 4 || type.intValue() == 5 || type.intValue() == 6){
						if(type.intValue() == 4){
							tencentType.add(1);
						}else if(type.intValue() == 5){
							tencentType.add(2);
						}else if(type.intValue() == 6){
							tencentType.add(3);
						}
					}
				}
				obj.setSinaVerifiedTypes(sinaType);
				obj.setTencentVerifiedTypes(tencentType);
			}
			//处理空值
			if(obj.getUserTypes() != null && obj.getUserTypes().size() == 0){
				obj.setUserTypes(null);
			}
			if(obj.getInteractTypes() != null && obj.getInteractTypes().size() == 0){
				obj.setInteractTypes(null);
			}
			if(obj.getPersonGroups() != null && obj.getPersonGroups().size() == 0){
				obj.setPersonGroups(null);
			}
			if(obj.getVerifiedTypeFlags() != null && obj.getVerifiedTypeFlags().size() == 0){
				obj.setVerifiedTypeFlags(null);
			}
			if(obj.getAccountIds() != null && obj.getAccountIds().size() == 0){
				obj.setAccountIds(null);
			}
			if(obj.getRpsGenders() != null && obj.getRpsGenders().size() == 0){
				obj.setRpsGenders(null);
			}
			if(obj.getDealPersonIds() != null && obj.getDealPersonIds().size() == 0){
				obj.setDealPersonIds(null);
			}
			if(obj.getSinaVerifiedTypes() != null && obj.getSinaVerifiedTypes().size() == 0){
				obj.setSinaVerifiedTypes(null);
			}
			if(obj.getTencentVerifiedTypes() != null && obj.getTencentVerifiedTypes().size() == 0){
				obj.setTencentVerifiedTypes(null);
			}
			List personList = personService.newQueryInteractDataList(obj);
			Integer listCount = personService.queryInteractDataListCount(obj);
			List letterMsgs = new ArrayList();
			List weixinMsgs = new ArrayList();
			List weiboMsgs = new ArrayList();
			Map personMap = new HashMap();
			for(int i = 0; i < personList.size(); i++){
				InteractListResultObj interactPerson = (InteractListResultObj) personList.get(i);
				if(interactPerson.getInteractType() == 5){//私信
					letterMsgs.add(interactPerson.getMessageId());
				}else if(interactPerson.getInteractType() == 6){//微信
					weixinMsgs.add(interactPerson.getMessageId());
				}else{//微博
					weiboMsgs.add(interactPerson.getMessageId());
				}
				Map tempMap = new HashMap();
				tempMap.put("accountIds", obj.getAccountIds());
				tempMap.put("personId", interactPerson.getPersonId());
				//2:查询互动用户头像
				List users = userService.getInteractUserImageList(tempMap);
				//3 查人员名称/运维人员ID/名称
				/*
				 * Map paramMap=new HashMap(); paramMap.put("personId",
				 * interactPerson.getPersonId()); paramMap.put("accountId",
				 * interactPerson.getAccountId()); Map
				 * instPersonMap=personService.queryInteractDataList2(paramMap);
				 * if(instPersonMap != null && instPersonMap.size() > 0){
				 * interactPerson.setPersonName((String)
				 * instPersonMap.get("person_name"));
				 * interactPerson.setInstPersonIds((String)
				 * instPersonMap.get("inst_person_ids"));
				 * interactPerson.setInstPersonNames((String)
				 * instPersonMap.get("inst_person_names")); }
				 */
				List imageList = new ArrayList();
				StringBuffer buff = new StringBuffer();
				StringBuffer accountNameBuff = new StringBuffer();
				int count = 0;
				Set imageSet = new HashSet();
				for(int j = 0; j < users.size(); j++){
					Map userMap = (Map) users.get(j);
					if(count < 4 && !imageSet.contains(userMap.get("user_id") + "" + userMap.get("account_type"))){
						imageSet.add(userMap.get("user_id") + "" + userMap.get("account_type"));
						String imageUrl = (String) userMap.get("image_url");
						if(StringUtils.isNotBlank(imageUrl)){
							imageList.add(userMap.get("image_url"));
						}

						count++;
					}
					String interactId = userMap.get("interact_id") + "";
					if(StringUtils.isNotBlank(interactId) && !buff.toString().contains(interactId)){
						buff.append(userMap.get("interact_id")).append(",");
					}
					String accountName = userMap.get("accountName") + "";
					if(j == 0){
						if(StringUtils.isNotBlank(accountName) && !accountNameBuff.toString().contains(accountName)){
							accountNameBuff.append(userMap.get("accountName")).append(",");
						}
					}
				}
				if(buff.length() > 0){
					buff.deleteCharAt(buff.length() - 1);
				}
				if(accountNameBuff.length() > 0){
					accountNameBuff.deleteCharAt(accountNameBuff.length() - 1);
				}
				/*
				 * for(int j=0;j<4;j++){
				 * imageList.add("http://tp2.sinaimg.cn/1258256457/50/5616578718/1"
				 * ); }
				 */
				//interactPerson.setContent("测试"+i);
				interactPerson.setHeadImages(imageList);
				interactPerson.setInteractIds(buff.toString());
				interactPerson.setAccountName(accountNameBuff.toString());
				personMap.put(interactPerson.getLastRecordId(), interactPerson);
			}
			//3:关联: 最近互动信息数据
			if(letterMsgs.size() > 0){
				List msgList = msgService.getInteractMessageListByIds(letterMsgs);
				setContent(personMap, msgList, 1);
			}
			if(weixinMsgs.size() > 0){
				List weixinList = weixinService.getInteractWeixinListByIds(weixinMsgs);
				setContent(personMap, weixinList, 2);
			}
			if(weiboMsgs.size() > 0){
				List weiboList = weiboService.getInteractWeiboListByIds(weiboMsgs);
				setContent(personMap, weiboList, 3);
			}
			//4:返回数据
			rsMap.put("datas", JsonUtil.list2json(personList));
			//5 查询记录总数
			rsMap.put("totalCount", listCount);
			//logger.info("--------rs="+StringUtil.getHttpResultStr(rsMap));
		}catch (Exception e){
			throw e;
		}
		return rsMap;
	}

	private void setContent(Map personMap,List msgList,Integer type){
		for(int i = 0; i < msgList.size(); i++){
			Map map = (Map) msgList.get(i);
			Long recordId = new Long(map.get("record_id") + "");
			String content = (String) map.get("content");
			String interactUserName = (String) map.get("screenName");
			String interactUserHeadImage = (String) map.get("imageUrl");
			String msgType = "0";
			String messageImage = "";
			String sourceId = "";
			InteractListResultObj interactPerson = (InteractListResultObj) personMap.get(recordId);
			if(interactPerson != null){
				interactPerson.setContent(content);
				//最新互动人员名称及头像
				interactPerson.setInteractUserName(interactUserName);
				interactPerson.setInteractUserHeadImage(interactUserHeadImage);
				if(type == 3){//转发、评论源微博id
					sourceId = map.get("sourceId").toString();
					interactPerson.setWeiboMid(sourceId + "");
				}
				if(map.containsKey("msg_type")){
					msgType = map.get("msg_type") + "";
					if(type == 2){
						if(msgType.equals("7")){//处理微信关注事件
							msgType = "1";//增加关注
						}else if(msgType.equals("8")){
							msgType = "2";//取消关注
						}else if(msgType.equals("4")){//视频消息
							msgType = "4";
						}else{
							msgType = "0";
						}
					}
					interactPerson.setEventType(msgType);
				}else{
					continue;
				}
				if(msgType.equals("5")){
					messageImage = (String) map.get("img");
					interactPerson.setMessageImage(messageImage);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception{
		InteractListQueryObj query = new InteractListQueryObj();
		List accounts = new ArrayList();
		accounts.add(180l);
		accounts.add(2l);
		query.setInstitutionId(1013l);
		query.setAccountIds(accounts);
		query.setStatus(0);//0 未分配 1 已分配 2 忽略 3 个人队列
		query.setInstPersonId(0l);
		query.setQueryName("artsio");
		/*
		 * List userTypes=new ArrayList(); userTypes.add(1); userTypes.add(2);
		 * query.setUserTypes(userTypes);
		 * query.setInteractTimeBegin(1390718465);
		 * query.setInteractTimeEnd(1390718466); List interactTypes=new
		 * ArrayList(); interactTypes.add(1); interactTypes.add(2);
		 * query.setInteractTypes(interactTypes); List personGroups=new
		 * ArrayList(); personGroups.add(1l); personGroups.add(2l);
		 * query.setPersonGroups(personGroups); query.setFollowersCountMin(1);
		 * query.setFollowersCountMax(1000);
		 */
		//List verifiedTypeFlags=new ArrayList();
		//verifiedTypeFlags.add(1);
		//verifiedTypeFlags.add(2);
		//query.setVerifiedTypeFlags(verifiedTypeFlags);
		List rpsGenders = new ArrayList();
		//rpsGenders.add("m");
		//rpsGenders.add("f");
		query.setRpsGenders(rpsGenders);
		query.setCountryCode("1");
		query.setRpsProvince("2");
		query.setRpsCity("3");
		query.setPage(1);
		query.setPageSize(15);
		query.setSortType("3");
		String paramJson = JsonUtil.bean2Json(query);
		TaskInteractListInterface task = new TaskInteractListInterface();
		Map paramMap = new HashMap();
		paramMap.put("paramJson", paramJson);
		task.cal(paramMap);
	}
}
