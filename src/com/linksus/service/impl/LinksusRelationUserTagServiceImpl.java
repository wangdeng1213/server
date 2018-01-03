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

	/** 查询列表 */
	public List<LinksusRelationUserTag> getLinksusRelationUserTagList(){
		return linksusRelationUserTagMapper.getLinksusRelationUserTagList();
	}

	/** 主键查询 */
	public LinksusRelationUserTag getLinksusRelationUserTagById(Long pid){
		return linksusRelationUserTagMapper.getLinksusRelationUserTagById(pid);
	}

	/** 新增 */
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

	/** 更新 */
	public Integer updateLinksusRelationUserTag(LinksusRelationUserTag entity){
		return linksusRelationUserTagMapper.updateLinksusRelationUserTag(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagById(Long pid){
		return linksusRelationUserTagMapper.deleteLinksusRelationUserTagById(pid);
	}

	public Integer deleteUserTagBySet(Map str){
		return linksusRelationUserTagMapper.deleteUserTagBySet(str);
	}

	/** 查询userId所有的标签数据 */
	public List<LinksusRelationUserTag> getTagsByUserId(Long userId){
		return linksusRelationUserTagMapper.getTagsByUserId(userId);
	}

	/** userid主键删除 */
	public Integer deleteLinksusRelationUserTagByUserId(Long pid){
		return linksusRelationUserTagMapper.deleteLinksusRelationUserTagById(pid);
	}

	/**判断userId 和 tagId 是否存在*/
	public Integer getCountByUserIdAndTagId(Long userId,Long tagId){
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("tagId", tagId);
		return linksusRelationUserTagMapper.getCountByUserIdAndTagId(map);
	}

	/** 将标签使用记录数插入中间表*/
	public Integer insertTagCountIntoMiddleTable(){
		return linksusRelationUserTagMapper.insertTagCountIntoMiddleTable();
	}

}