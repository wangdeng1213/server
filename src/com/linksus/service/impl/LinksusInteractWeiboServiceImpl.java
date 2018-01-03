package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractWeiboMapper;
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractWeiboService;

@Service("linksusInteractWeiboService")
public class LinksusInteractWeiboServiceImpl extends BaseService implements LinksusInteractWeiboService{

	@Autowired
	private LinksusInteractWeiboMapper linksusInteractWeiboMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractWeibo> getLinksusInteractWeiboList(){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboList();
	}

	/** ������ѯ */
	public LinksusInteractWeibo getLinksusInteractWeiboById(Long pid){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboById(pid);
	}

	/** ���� */

	public Integer insertLinksusInteractWeibo(LinksusInteractWeibo entity){
		return linksusInteractWeiboMapper.insertLinksusInteractWeibo(entity);
	}

	/** ���� */
	/*
	 * public Integer updateLinksusInteractWeibo(LinksusInteractWeibo entity){
	 * return linksusInteractWeiboMapper.updateLinksusInteractWeibo(entity); }
	 */

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeiboById(Long pid){
		return linksusInteractWeiboMapper.deleteLinksusInteractWeiboById(pid);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeiboByMap(Map map){
		return linksusInteractWeiboMapper.deleteLinksusInteractWeiboByMap(map);
	}

	/** <!--ȡ���ظ���Ϣ�������б�  -->*/
	public List<LinksusInteractWeibo> getLinksusInteractWeiboTaskList(LinksusInteractWeibo entity){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboTaskList(entity);

	}

	/** <!--ȡ���ظ���Ϣ�Ķ�ʱ���������б�  -->*/
	/*
	 * public List<LinksusInteractWeibo>
	 * getWeiboReplyPrepare(LinksusInteractWeibo entity){ return
	 * linksusInteractWeiboMapper.getWeiboReplyPrepare(entity); }
	 */
	//���·��ص� ��Ϣ
	public void updateSendReplySucc(LinksusInteractWeibo entity){
		linksusInteractWeiboMapper.updateSendReplySucc(entity);
	}

	@Override
	public void updateSendReplyStatus(LinksusInteractWeibo entity){
		linksusInteractWeiboMapper.updateSendReplyStatus(entity);
	}

	/**��ѯ΢��������¼�Ƿ����*/
	public LinksusInteractWeibo getInteractWeiboIsExists(Map map){
		return linksusInteractWeiboMapper.getInteractWeiboIsExists(map);
	}

	/** ����mid��ѯ�����б� */
	public List getInteractWeiboListByMid(Map map){
		return linksusInteractWeiboMapper.getInteractWeiboListByMid(map);
	}

	/** ����������ѯ��Ҫ�ظ����۵�΢��*/
	public LinksusInteractWeibo getReplyWeiboById(Long pid){
		return linksusInteractWeiboMapper.getReplyWeiboById(pid);
	}

	public List getInteractWeiboListByIds(List weiboMsgs){
		return linksusInteractWeiboMapper.getInteractWeiboListByIds(weiboMsgs);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusInteractWeibo entity = (LinksusInteractWeibo) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.LinksusInteractWeiboMapper.insertLinksusInteractWeibo", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}