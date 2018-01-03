package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationWxaccountMapper;
import com.linksus.entity.LinksusRelationWxaccount;
import com.linksus.service.LinksusRelationWxaccountService;

@Service("linksusRelationWxaccountService")
public class LinksusRelationWxaccountServiceImpl implements LinksusRelationWxaccountService{

	@Autowired
	private LinksusRelationWxaccountMapper linksusRelationWxaccountMapper;

	/** 查询列表 */
	public List<LinksusRelationWxaccount> getLinksusRelationWxaccountList(){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountList();
	}

	/** 主键查询 */
	public LinksusRelationWxaccount getLinksusRelationWxaccountById(Long pid){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationWxaccount(LinksusRelationWxaccount entity){
		return linksusRelationWxaccountMapper.insertLinksusRelationWxaccount(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationWxaccount(LinksusRelationWxaccount entity){
		return linksusRelationWxaccountMapper.updateLinksusRelationWxaccount(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationWxaccountById(Long pid){
		return linksusRelationWxaccountMapper.deleteLinksusRelationWxaccountById(pid);
	}

	/** 查询Msg_MD5是否存在 */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByMsgMD5(String msgMd5){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountByMsgMD5(msgMd5);
	}

	/** 根据复合主键查询  */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByPrimary(Map map){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountByPrimary(map);
	}
}