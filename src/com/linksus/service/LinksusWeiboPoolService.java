package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;

public interface LinksusWeiboPoolService{

	/** 查询列表 */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList();

	/** 主键查询 */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid);

	/** 新增 */
	// public Integer insertLinksusWeiboPool(LinksusWeiboPool entity);
	/** 更新 */
	//	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity);

	/** 微博互动更新微博评论数和转发数*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity);

	/** 主键删除 */
	public Integer deleteLinksusWeiboPoolById(Long pid);

	/** 查询列表 */
	public List<LinksusWeibo> getLinksusWeiboPoolSend(LinksusWeibo entity);

	public LinksusWeibo getLinksusWeiboPoolByMap(Map map);

	/** 更新 */
	public Integer updateLinksusWeiboPoolCount(LinksusWeibo entity);

	/** 通过mid 和 accountType 判断微博是否存在*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap);

}
