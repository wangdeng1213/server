package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonContactMapper;
import com.linksus.entity.LinksusRelationPersonContact;
import com.linksus.service.LinksusRelationPersonContactService;

@Service("linksusRelationPersonContactService")
public class LinksusRelationPersonContactServiceImpl implements LinksusRelationPersonContactService{

	@Autowired
	private LinksusRelationPersonContactMapper linksusRelationPersonContactMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonContact> getLinksusRelationPersonContactList(){
		return linksusRelationPersonContactMapper.getLinksusRelationPersonContactList();
	}

	/** 主键查询 */
	public LinksusRelationPersonContact getLinksusRelationPersonContactById(Long pid){
		return linksusRelationPersonContactMapper.getLinksusRelationPersonContactById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonContact(LinksusRelationPersonContact entity){
		return linksusRelationPersonContactMapper.insertLinksusRelationPersonContact(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonContact(LinksusRelationPersonContact entity){
		return linksusRelationPersonContactMapper.updateLinksusRelationPersonContact(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonContactById(Long pid){
		return linksusRelationPersonContactMapper.deleteLinksusRelationPersonContactById(pid);
	}

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonContactMapper.deleteByPersonId(personId);
	}

	/** 通过person_id查询需要发短信用户信息 */
	public List<LinksusRelationPersonContact> getPersonContactMobileListByPersonId(long personId){
		return linksusRelationPersonContactMapper.getPersonContactMobileListByPersonId(personId);
	}

	/** 通过person_id查询需要发邮件用户信息 */
	public List<LinksusRelationPersonContact> getPersonContactEmailListByPersonId(long personId){
		return linksusRelationPersonContactMapper.getPersonContactEmailListByPersonId(personId);
	}

}