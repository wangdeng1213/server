package com.linksus.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserTagMapper;
import com.linksus.entity.LinksusRelationUserTag;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserTagService;

@Service("linksusRelationUserTagService")
public class LinksusRelationUserTagServiceImpl extends BaseService implements LinksusRelationUserTagService{

	@Autowired
	private LinksusRelationUserTagMapper linksusRelationUserTagMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationUserTag> getLinksusRelationUserTagList(){
		return linksusRelationUserTagMapper.getLinksusRelationUserTagList();
	}

	/** ������ѯ */
	public LinksusRelationUserTag getLinksusRelationUserTagById(Long pid){
		return linksusRelationUserTagMapper.getLinksusRelationUserTagById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationUserTag(LinksusRelationUserTag entity){
		return linksusRelationUserTagMapper.insertLinksusRelationUserTag(entity);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserTag entity = (LinksusRelationUserTag) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.linksusRelationUserTagMapper.insertLinksusRelationUserTag",
							entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	/** ���� */
	public Integer updateLinksusRelationUserTag(LinksusRelationUserTag entity){
		return linksusRelationUserTagMapper.updateLinksusRelationUserTag(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagById(Long pid){
		return linksusRelationUserTagMapper.deleteLinksusRelationUserTagById(pid);
	}

	public Integer deleteUserTagBySet(Map str){
		return linksusRelationUserTagMapper.deleteUserTagBySet(str);
	}

	/** ��ѯuserId���еı�ǩ���� */
	public List<LinksusRelationUserTag> getTagsByUserId(Long userId){
		return linksusRelationUserTagMapper.getTagsByUserId(userId);
	}

	/** userid����ɾ�� */
	public Integer deleteLinksusRelationUserTagByUserId(Long pid){
		return linksusRelationUserTagMapper.deleteLinksusRelationUserTagById(pid);
	}

	/**�ж�userId �� tagId �Ƿ����*/
	public Integer getCountByUserIdAndTagId(Long userId,Long tagId){
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("tagId", tagId);
		return linksusRelationUserTagMapper.getCountByUserIdAndTagId(map);
	}

	/** ����ǩʹ�ü�¼�������м��*/
	public Integer insertTagCountIntoMiddleTable(){
		return linksusRelationUserTagMapper.insertTagCountIntoMiddleTable();
	}

}