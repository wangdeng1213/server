package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWxaccount;

public interface LinksusRelationWxaccountMapper{

	/** 列表查询 */
	public List<LinksusRelationWxaccount> getLinksusRelationWxaccountList();

	/** 主键查询 */
	public LinksusRelationWxaccount getLinksusRelationWxaccountById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationWxaccount(LinksusRelationWxaccount entity);

	/** 更新 */
	public Integer updateLinksusRelationWxaccount(LinksusRelationWxaccount entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationWxaccountById(Long pid);

	/** 查询Msg_MD5是否存在 */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByMsgMD5(String msgMd5);

	/** 根据复合主键查询  */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByPrimary(Map map);

}
