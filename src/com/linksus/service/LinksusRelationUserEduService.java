package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationUserEdu;

public interface LinksusRelationUserEduService{

	/** 查询列表 */
	public List<LinksusRelationUserEdu> getLinksusRelationUserEduList();

	/** 主键查询 */
	public LinksusRelationUserEdu getLinksusRelationUserEduById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserEdu(LinksusRelationUserEdu entity);

	/** 更新 */
	public Integer updateLinksusRelationUserEdu(LinksusRelationUserEdu entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserEduById(Long pid);

	public Integer deleteLinksusRelationUserEduByUserId(Long userId);

}
