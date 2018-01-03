package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskWeiboInteract;

public interface LinksusTaskWeiboInteractService{

	/** ��ѯ�б� */
	public List<LinksusTaskWeiboInteract> getLinksusTaskWeiboInteractList();

	/** ������ѯ */
	public LinksusTaskWeiboInteract getLinksusTaskWeiboInteractById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** ���� */
	public Integer updateLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskWeiboInteractById(Long pid);

	/** ��ȡ���id */
	public LinksusTaskWeiboInteract getMaxIdByAccountId(LinksusTaskWeiboInteract linksusTaskWeiboInteract);

	/** ���õ�ǰ�˺ŷ�ҳ��Ϣ*/
	public Integer updateLinksusTaskWeiboInteractPageInfo(LinksusTaskWeiboInteract linksusTaskWeiboInteract);
}
