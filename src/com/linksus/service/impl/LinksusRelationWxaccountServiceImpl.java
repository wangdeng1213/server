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

	/** ��ѯ�б� */
	public List<LinksusRelationWxaccount> getLinksusRelationWxaccountList(){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountList();
	}

	/** ������ѯ */
	public LinksusRelationWxaccount getLinksusRelationWxaccountById(Long pid){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationWxaccount(LinksusRelationWxaccount entity){
		return linksusRelationWxaccountMapper.insertLinksusRelationWxaccount(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationWxaccount(LinksusRelationWxaccount entity){
		return linksusRelationWxaccountMapper.updateLinksusRelationWxaccount(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWxaccountById(Long pid){
		return linksusRelationWxaccountMapper.deleteLinksusRelationWxaccountById(pid);
	}

	/** ��ѯMsg_MD5�Ƿ���� */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByMsgMD5(String msgMd5){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountByMsgMD5(msgMd5);
	}

	/** ���ݸ���������ѯ  */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByPrimary(Map map){
		return linksusRelationWxaccountMapper.getLinksusRelationWxaccountByPrimary(map);
	}
}