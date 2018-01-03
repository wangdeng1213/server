package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskWeiboInteract;

public interface LinksusTaskWeiboInteractMapper{

	/** �б��ѯ */
	public List<LinksusTaskWeiboInteract> getLinksusTaskWeiboInteractList();

	/** ������ѯ */
	public LinksusTaskWeiboInteract getLinksusTaskWeiboInteractById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** ���� */
	public Integer updateLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskWeiboInteractById(Long pid);

	public LinksusTaskWeiboInteract getMaxIdByAccountId(LinksusTaskWeiboInteract linksusTaskWeiboInteract);

	/** ���õ�ǰ�˺ŷ�ҳ��Ϣ*/
	public Integer updateLinksusTaskWeiboInteractPageInfo(LinksusTaskWeiboInteract linksusTaskWeiboInteract);
}
