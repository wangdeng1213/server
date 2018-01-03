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
	 * �����û���ǩ����-�����û�(���ж��û���ǩ��)
	 * @param tagsSet
	 * @param accountType
	 * @param userId
	 * @throws Exception
	 */
	public static void userTagInsert(Set tagsSet,String accountType,String userId) throws Exception{
		if(tagsSet != null && !tagsSet.isEmpty()){
			//�ӱ�ǩ�����в�ѯ��ǩ�Ƿ����
			Map tagParaMap = new HashMap();
			tagParaMap.put("tagsSet", tagsSet);
			tagParaMap.put("accountType", accountType);
			//�ӱ�ǩ������ȡ����List�еĶ���
			List<LinksusRelationUserTagdef> userTagdefList = linksusUserTagdefService.getUserTagdefBySet(tagParaMap);
			Map tagMap = new HashMap();//����ѯ���ı�ǩ�ŵ�tagMap��
			if(userTagdefList != null && userTagdefList.size() > 0){
				for(int i = 0; i < userTagdefList.size(); i++){
					LinksusRelationUserTagdef tagdef = userTagdefList.get(i);
					tagMap.put(tagdef.getTag(), tagdef);
				}
			}
			for(Iterator itor = tagsSet.iterator(); itor.hasNext();){
				String tagStr = (String) itor.next();
				if(tagMap.containsKey(tagStr)){//���tagMap�а����ñ�ǩ �û������ǩ���д���
					LinksusRelationUserTagdef userTagdeg = (LinksusRelationUserTagdef) tagMap.get(tagStr);
					//�����û���ǩ��
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
	 * �û�����ʱ��ǩ����(���ж��û���ǩ��)
	 * @param tagsSet
	 * @param accountType
	 * @param userId
	 * @throws Exception
	 */
	public static void userExsitTagsData(Set tagsSet,String accountType,String userId) throws Exception{
		if(tagsSet != null && !tagsSet.isEmpty()){
			//�û����� �ȴ��û���ǩ���в�ѯ�Ƿ��������û���ǩ
			//��tagsList�����û���ǩ����
			Map tagParaMap = new HashMap();
			tagParaMap.put("tagsSet", tagsSet);
			tagParaMap.put("userId", userId);
			//1 ��ѯ�û����еı�ǩ
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
			//2 ԭ�û���ǩ����set�е�ɾ��
			if(!delSet.isEmpty()){
				Map paramMap = new HashMap();
				paramMap.put("userId", userId);
				paramMap.put("tagsSet", delSet);
				linksusUserTagService.deleteUserTagBySet(paramMap);
			}
			//3 set�еı�ǩ����ԭ��ǩ�е�����
			userTagInsert(tagsSet, accountType, userId);
		}
	}

	/**
	 * ͨ��URLȡ����΢���û���ǩ
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
	 * �������˱�ǩ
	 * @param tagStr
	 * @return
	 */
	public static Set getSinaUserTags(String tagStr){
		List tagResultList = JsonUtil.json2list(tagStr, Map.class);
		//��ǩ����δ����
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
	 * ��ȡ��Ѷ�û���ǩ
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
