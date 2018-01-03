package com.linksus.common.module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.entity.LinksusRelationUserTag;
import com.linksus.entity.LinksusRelationUserTagdef;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationUserTagService;
import com.linksus.service.LinksusRelationUserTagdefService;

public class UserTagCommon{

	protected static final Logger logger = LoggerFactory.getLogger(UserTagCommon.class);
	private static LinksusRelationUserTagService linksusUserTagService = (LinksusRelationUserTagService) ContextUtil
			.getBean("linksusRelationUserTagService");
	private static LinksusRelationUserTagdefService linksusUserTagdefService = (LinksusRelationUserTagdefService) ContextUtil
			.getBean("linksusRelationUserTagdefService");

	/**
	 * 插入用户标签数据-新增用户(不判断用户标签表)
	 * @param tagsSet
	 * @param accountType
	 * @param userId
	 * @throws Exception
	 */
	public static void userTagInsert(Set tagsSet,String accountType,String userId) throws Exception{
		if(tagsSet != null && !tagsSet.isEmpty()){
			//从标签主表中查询标签是否存在
			Map tagParaMap = new HashMap();
			tagParaMap.put("tagsSet", tagsSet);
			tagParaMap.put("accountType", accountType);
			//从标签主表中取出在List中的对象
			List<LinksusRelationUserTagdef> userTagdefList = linksusUserTagdefService.getUserTagdefBySet(tagParaMap);
			Map tagMap = new HashMap();//将查询出的标签放到tagMap中
			if(userTagdefList != null && userTagdefList.size() > 0){
				for(int i = 0; i < userTagdefList.size(); i++){
					LinksusRelationUserTagdef tagdef = userTagdefList.get(i);
					tagMap.put(tagdef.getTag(), tagdef);
				}
			}
			for(Iterator itor = tagsSet.iterator(); itor.hasNext();){
				String tagStr = (String) itor.next();
				if(tagMap.containsKey(tagStr)){//如果tagMap中包含该标签 用户定义标签表中存在
					LinksusRelationUserTagdef userTagdeg = (LinksusRelationUserTagdef) tagMap.get(tagStr);
					//插入用户标签表
					LinksusRelationUserTag userTag = new LinksusRelationUserTag();
					userTag.setTagId(userTagdeg.getPid());
					userTag.setUserId(new Long(userId));
					userTag.setTag(tagStr);
					// QueueDataSave.addDataToQueue(userTag, Constants.OPER_TYPE_INSERT);
					linksusUserTagService.insertLinksusRelationUserTag(userTag);
				}else{
					Long tagId = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_tagdef", "pid");
					LinksusRelationUserTagdef userTagdef = new LinksusRelationUserTagdef();
					userTagdef.setPid(tagId);
					userTagdef.setTagType(Integer.valueOf(accountType));
					userTagdef.setTag(tagStr);
					userTagdef.setUseCount(1);
					try{
						//	 QueueDataSave.addDataToQueue(userTagdef, Constants.OPER_TYPE_INSERT);
						linksusUserTagdefService.insertLinksusRelationUserTagdef(userTagdef);
					}catch (Exception e){
						LogUtil.saveException(logger, new Exception("tagMap>>>>>>>>>>" + tagMap + "tagStr>>>>>>>>>>"
								+ tagStr + "pid>>>>>>>>" + tagId + "accountType>>>>>>" + accountType));
					}
					LinksusRelationUserTag userTag = new LinksusRelationUserTag();
					userTag.setTagId(tagId);
					userTag.setUserId(new Long(userId));
					userTag.setTag(tagStr);
					// QueueDataSave.addDataToQueue(userTag, Constants.OPER_TYPE_INSERT);
					linksusUserTagService.insertLinksusRelationUserTag(userTag);
				}
			}
		}
	}

	/**
	 * 用户存在时标签处理(需判断用户标签表)
	 * @param tagsSet
	 * @param accountType
	 * @param userId
	 * @throws Exception
	 */
	public static void userExsitTagsData(Set tagsSet,String accountType,String userId) throws Exception{
		if(tagsSet != null && !tagsSet.isEmpty()){
			//用户存在 先从用户标签表中查询是否有新增用户标签
			//将tagsList传到用户标签表中
			Map tagParaMap = new HashMap();
			tagParaMap.put("tagsSet", tagsSet);
			tagParaMap.put("userId", userId);
			//1 查询用户所有的标签
			Set delSet = new HashSet();
			List<LinksusRelationUserTag> userTagList = linksusUserTagService.getTagsByUserId(new Long(userId));
			for(LinksusRelationUserTag userTag : userTagList){
				String tag = userTag.getTag();
				if(!tagsSet.contains(tag)){
					delSet.add(userTag.getTagId());
				}else{
					tagsSet.remove(tag);
				}
			}
			//2 原用户标签不在set中的删除
			if(!delSet.isEmpty()){
				Map paramMap = new HashMap();
				paramMap.put("userId", userId);
				paramMap.put("tagsSet", delSet);
				linksusUserTagService.deleteUserTagBySet(paramMap);
			}
			//3 set中的标签不在原标签中的新增
			userTagInsert(tagsSet, accountType, userId);
		}
	}

	/**
	 * 通过URL取新浪微博用户标签
	 * @param paraMap
	 * @return
	 */
	public static Set getSinaUserTagsByUrl(Map paraMap){
		UrlEntity strUrlTag = LoadConfig.getUrlEntity("WeiBoTagData");
		Map paramsTag = new HashMap();
		paramsTag.put("access_token", paraMap.get("token").toString());
		paramsTag.put("uid", paraMap.get("uid").toString());
		paramsTag.put("count", 200);
		String resultTag = HttpUtil.getRequest(strUrlTag, paramsTag);
		return getSinaUserTags(resultTag);
	}

	/**
	 * 解析新浪标签
	 * @param tagStr
	 * @return
	 */
	public static Set getSinaUserTags(String tagStr){
		List tagResultList = JsonUtil.json2list(tagStr, Map.class);
		//标签处理未处理
		Set tagsSet = new HashSet();
		String value = "";
		if(tagResultList != null && tagResultList.size() > 0){
			for(int j = 0; j < tagResultList.size(); j++){
				Map map = (Map) tagResultList.get(j);
				Set keySet = map.keySet();
				Iterator iterator = keySet.iterator();
				while (iterator.hasNext()){
					String key = (String) iterator.next();
					if(!"weight".equals(key) && !"flag".equals(key)){
						value = map.get(key) + "";
					}
				}
				tagsSet.add(value);
			}
		}
		return tagsSet;
	}

	/**
	 * 获取腾讯用户标签
	 * @param list
	 * @return
	 */
	public static Set getTencentUserTags(List<Map> list){
		Set tagsSet = new HashSet();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map nameMap = list.get(i);
				String nameStr = nameMap.get("name").toString();
				tagsSet.add(nameStr);
			}
		}
		return tagsSet;
	}
}
