package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.AttentionRelationEntrity;

public interface AttentionRelationEntrityService{

	/** ��ѯ�б� */
	public List<AttentionRelationEntrity> getAttentionRelationEntrityList(Map map);

	/** ������ѯ */
	/*
	 * public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long
	 * pid);
	 *//** ���� */
	/*
	 * public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser
	 * entity);
	 *//** ���� */
	/*
	 * public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser
	 * entity);
	 *//** ����ɾ�� */
	/*
	 * public Integer deleteLinksusRelationWeibouserById(Long pid);
	 */
	public List<AttentionRelationEntrity> getRelationAttentionTask(Map map);

}
