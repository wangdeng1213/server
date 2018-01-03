package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;

public interface LinksusWeiboPoolMapper{

	/** 列表查询 */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList();

	/** 主键查询 */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid);

	/** 新增 */
	public Integer insertLinksusWeiboPool(LinksusWeiboPool entity);

	/** 更新 */
	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity);

	/** 微博互动更新微博评论数和转发数*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity);

	/** 主键删除 */
	public Integer deleteLinksusWeiboPoolById(Long pid);

	public List<LinksusWeibo> getLinksusWeiboPoolSend(LinksusWeibo entity);

	public LinksusWeibo getLinksusWeiboPoolByMap(Map map);

	public Integer updateLinksusWeiboPoolCount(LinksusWeibo entity);

	/** 检查微博是否存在*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap);
}
