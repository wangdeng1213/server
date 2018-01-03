package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserTagdefMapper;
import com.linksus.entity.LinksusRelationUserTagdef;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserTagdefService;

@Service("linksusRelationUserTagdefService")
public class LinksusRelationUserTagdefServiceImpl extends BaseService implements LinksusRelationUserTagdefService{

	@Autowired
	private LinksusRelationUserTagdefMapper linksusRelationUserTagdefMapper;

	/** 查询列表 */
	public List<LinksusRelationUserTagdef> getLinksusRelationUserTagdefList(){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefList();
	}

	/** 主键查询 */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefById(Long pid){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationUserTagdef(LinksusRelationUserTagdef entity){
		return linksusRelationUserTagdefMapper.insertLinksusRelationUserTagdef(entity);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserTagdef entity = (LinksusRelationUserTagdef) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert(
							"com.linksus.dao.linksusRelationUserTagdefMapper.insertLinksusRelationUserTagdef", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	/** 更新 */
	public Integer updateLinksusRelationUserTagdef(LinksusRelationUserTagdef entity){
		return linksusRelationUserTagdefMapper.updateLinksusRelationUserTagdef(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagdefById(Long pid){
		return linksusRelationUserTagdefMapper.deleteLinksusRelationUserTagdefById(pid);
	}

	/** 查询存在list中的标签对象 */
	public List<LinksusRelationUserTagdef> getUserTagdefBySet(Map tagParaMap){
		return linksusRelationUserTagdefMapper.getUserTagdefBySet(tagParaMap);
	}

	/**通过标签 和 用户类型判断标签主表中是否存在*/

	public LinksusRelationUserTagdef checkIsExsitByTagAndaccoutType(Map paraMap){
		return linksusRelationUserTagdefMapper.checkIsExsitByTagAndaccoutType(paraMap);
	}

	/** 主键查询 */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefByTagName(String tagname){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefByTagName(tagname);
	}

	/** 标签使用次数统计更新 */
	public Integer updateLinksusRelationUserTagdefUseCount(){
		return linksusRelationUserTagdefMapper.updateLinksusRelationUserTagdefUseCount();
	}

	/** 清空用户标签统计使用的中间表 */
	public Integer clearUserTagMiddleTable(){
		return linksusRelationUserTagdefMapper.clearUserTagMiddleTable();
	}
}