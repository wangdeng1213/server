package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.AttentionRelationEntrity;

public interface AttentionRelationEntrityService{

	/** 查询列表 */
	public List<AttentionRelationEntrity> getAttentionRelationEntrityList(Map map);

	/** 主键查询 */
	/*
	 * public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long
	 * pid);
	 *//** 新增 */
	/*
	 * public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser
	 * entity);
	 *//** 更新 */
	/*
	 * public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser
	 * entity);
	 *//** 主键删除 */
	/*
	 * public Integer deleteLinksusRelationWeibouserById(Long pid);
	 */
	public List<AttentionRelationEntrity> getRelationAttentionTask(Map map);

}
