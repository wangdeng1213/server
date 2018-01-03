package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWeibouser;

public interface LinksusRelationWeibouserService{

	/** ��ѯ�б� */
	public List<LinksusRelationWeibouser> getLinksusRelationWeibouserList(String accountType);

	/** ������ѯ */
	public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long pid);

	/** ���� */
	//public Integer insertLinksusRelationWeibouser(LinksusRelationWeibouser entity);
	//public void saveLinksusRelationWeibouser(List userList,String operType);
	/** ���� */
	//public Integer updateLinksusRelationWeibouser(LinksusRelationWeibouser entity);
	/** ����ɾ�� */
	/** ��ѯһ������ǰ��΢���û����� ִ�и���*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByTime(LinksusRelationWeibouser entity);

	/** ��ѯָ������֮ǰ��΢���û����� ִ�и���*/
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByLimitDay(LinksusRelationWeibouser entity);

	/** ����΢����˿��������ʱ��*/
	public Integer updateWeibouserLastSytime(LinksusRelationWeibouser entity);

	/** ��������΢����˿��������ʱ��*/
	public void updateWeibouserLastSytimeList(List<LinksusRelationWeibouser> dataList,String operType);

	/** ����΢����˿��΢�����ID*/
	public Integer updateWeibouserLastWeiboId(LinksusRelationWeibouser entity);

	public Integer deleteLinksusRelationWeibouserById(Long pid);

	public int getCountByrpsIdAndType(Map map);

	//����û��Ƿ����
	public Integer checkUserIsExsit(Map paraMap);

	public LinksusRelationWeibouser getWeibouserByrpsIdAndType(Map paraMap);

	public Integer updateSinaLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	public Integer updateCententLinksusRelationWeibouser(LinksusRelationWeibouser entity);

	/** ҵ��������ѯ */
	public LinksusRelationWeibouser getLinksusRelationWeibouser(LinksusRelationWeibouser dto);

	public LinksusRelationWeibouser getRelationWeibouserByUserId(long parseLong);

	/** ����PersonId */
	public Integer updatePersonId(LinksusRelationWeibouser entity);

	public List<LinksusRelationWeibouser> getLinksusTencentWeibouserList(LinksusRelationWeibouser entity);

	public Integer updateLinksusRelationWeibouserById(LinksusRelationWeibouser entity);

	//�б�
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByMap(Map map);

	/** ��ѯ�����˻����ǳ� */
	public String getWeibouserNameByAccountId(Long accountId);

}
