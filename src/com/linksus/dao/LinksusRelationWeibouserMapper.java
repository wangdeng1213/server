package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWeibouser;

public interface LinksusRelationWeibouserMapper{

	/** 列表查询 */
	public List<LinksusRelationWeibouser> getLinksusRelationWeibouserList(String accountType);

	/** 主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** 更新 */
	public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationWeibouserById(Long pid);

	public List<LinksusRelationWeibouser> getLinksusWeibouserListByTime(LinksusRelationWeibouser entity);

	/** 查询指定天数以前的微博用户*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByDay(LinksusRelationWeibouser entity);

	/** 更新微博粉丝的最后更新时间*/
	public Integer updateWeibouserLastSytime(LinksusRelationWeibouser entity);

	/** 更新微博粉丝的微博最大ID*/
	public Integer updateWeibouserLastWeiboId(LinksusRelationWeibouser entity);

	public Integer getCountByrpsIdAndType(Map map);

	public LinksusRelationWeibouser getWeibouserByrpsIdAndType(Map paraMap);

	public Integer updateSinaLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	public Integer updateCententLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** 业务主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouser(LinksusRelationWeibouser dto);

	public List<LinksusRelationWeibouser> getLinksusTencentWeibouserList(LinksusRelationWeibouser entity);

	public LinksusRelationWeibouser getRelationWeibouserByUserId(long parseLong);

	public Integer updateLinksusRelationWeibouserById(LinksusRelationWeibouser entity);

	public Integer checkUserIsExsit(Map paraMap);

	/** 更新PersonId */
	public Integer updatePersonId(LinksusRelationWeibouser entity);

	//列表
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByMap(Map map);

	/** 查询新浪账户的昵称 */
	public String getWeibouserNameByAccountId(Long accountId);
}
