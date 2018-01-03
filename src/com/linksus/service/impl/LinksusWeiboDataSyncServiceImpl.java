package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWeiboDataSyncMapper;
import com.linksus.entity.LinksusWeiboDataSync;
import com.linksus.service.LinksusWeiboDataSyncService;

@Service("linksusWeiboDataSyncService")
public class LinksusWeiboDataSyncServiceImpl implements LinksusWeiboDataSyncService{

	@Autowired
	private LinksusWeiboDataSyncMapper LinksusWeiboDataSyncMapper;

	@Override
	public void updateLinksusWeiboDataSync(List<LinksusWeiboDataSync> list){
		LinksusWeiboDataSyncMapper.updateLinksusWeiboDataSync(list);
	}

	@Override
	public List<LinksusWeiboDataSync> getLinksusWeiboDataSyncList(){
		// TODO Auto-generated method stub
		return LinksusWeiboDataSyncMapper.getLinksusWeiboDataSyncList();
	}

	@Override
	public void updateLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync){
		// TODO Auto-generated method stub
		LinksusWeiboDataSyncMapper.updateLinksusWeiboDataSyncSingle(linksusWeiboDataSync);

	}

	@Override
	public Integer getLinksusWeiboDataSyncCount(LinksusWeiboDataSync linksusWeiboDataSync){
		// TODO Auto-generated method stub
		return LinksusWeiboDataSyncMapper.getLinksusWeiboDataSyncCount(linksusWeiboDataSync);
	}

	@Override
	public void insertLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync){
		// TODO Auto-generated method stub
		LinksusWeiboDataSyncMapper.insertLinksusWeiboDataSyncSingle(linksusWeiboDataSync);
	}
}
