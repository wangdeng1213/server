package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWeibouser;

public interface LinksusRelationWeibouserMapper{

	/** �б��ѯ */
	public List<LinksusRelationWeibouser> getLinksusRelationWeibouserList(String accountType);

	/** ������ѯ */
	public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** ���� */
	public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWeibouserById(Long pid);

	public List<LinksusRelationWeibouser> getLinksusWeibouserListByTime(LinksusRelationWeibouser entity);

	/** ��ѯָ��������ǰ��΢���û�*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByDay(LinksusRelationWeibouser entity);

	/** ����΢����˿��������ʱ��*/
	public Integer updateWeibouserLastSytime(LinksusRelationWeibouser entity);

	/** ����΢����˿��΢�����ID*/
	public Integer updateWeibouserLastWeiboId(LinksusRelationWeibouser entity);

	public Integer getCountByrpsIdAndType(Map map);

	public LinksusRelationWeibouser getWeibouserByrpsIdAndType(Map paraMap);

	public Integer updateSinaLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	public Integer updateCententLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** ҵ��������ѯ */
	public LinksusRelationWeibouser getLinksusRelationWeibouser(LinksusRelationWeibouser dto);

	public List<LinksusRelationWeibouser> getLinksusTencentWeibouserList(LinksusRelationWeibouser entity);

	public LinksusRelationWeibouser getRelationWeibouserByUserId(long parseLong);

	public Integer updateLinksusRelationWeibouserById(LinksusRelationWeibouser entity);

	public Integer checkUserIsExsit(Map paraMap);

	/** ����PersonId */
	public Integer updatePersonId(LinksusRelationWeibouser entity);

	//�б�
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByMap(Map map);

	/** ��ѯ�����˻����ǳ� */
	public String getWeibouserNameByAccountId(Long accountId);
}
