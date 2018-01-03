package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWeibouser;

public interface LinksusRelationWeibouserService{

	/** 查询列表 */
	public List<LinksusRelationWeibouser> getLinksusRelationWeibouserList(String accountType);

	/** 主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long pid);

	/** 新增 */
	//public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser entity);
	//public void saveLinksusRelationWeibouser(List userList,String operType);
	/** 更新 */
	//public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser entity);
	/** 主键删除 */
	/** 查询一个月以前的微博用户数据 执行更新*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByTime(LinksusRelationWeibouser entity);

	/** 查询指定日期之前的微博用户数据 执行更新*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByLimitDay(LinksusRelationWeibouser entity);

	/** 更新微博粉丝的最后更新时间*/
	public Integer updateWeibouserLastSytime(LinksusRelationWeibouser entity);

	/** 批量更新微博粉丝的最后更新时间*/
	public void updateWeibouserLastSytimeList(List<LinksusRelationWeibouser> dataList,String operType);

	/** 更新微博粉丝的微博最大ID*/
	public Integer updateWeibouserLastWeiboId(LinksusRelationWeibouser entity);

	public Integer deleteLinksusRelationWeibouserById(Long pid);

	public int getCountByrpsIdAndType(Map map);

	//检查用户是否存在
	public Integer checkUserIsExsit(Map paraMap);

	public LinksusRelationWeibouser getWeibouserByrpsIdAndType(Map paraMap);

	public Integer updateSinaLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	public Integer updateCententLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** 业务主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouser(LinksusRelationWeibouser dto);

	public LinksusRelationWeibouser getRelationWeibouserByUserId(long parseLong);

	/** 更新PersonId */
	public Integer updatePersonId(LinksusRelationWeibouser entity);

	public List<LinksusRelationWeibouser> getLinksusTencentWeibouserList(LinksusRelationWeibouser entity);

	public Integer updateLinksusRelationWeibouserById(LinksusRelationWeibouser entity);

	//列表
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByMap(Map map);

	/** 查询新浪账户的昵称 */
	public String getWeibouserNameByAccountId(Long accountId);

}
