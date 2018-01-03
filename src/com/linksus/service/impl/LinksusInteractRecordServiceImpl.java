package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractRecordMapper;
import com.linksus.entity.LinksusInteractRecord;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractRecordService;

@Service("linksusInteractRecordService")
public class LinksusInteractRecordServiceImpl extends BaseService implements LinksusInteractRecordService{

	@Autowired
	private LinksusInteractRecordMapper linksusInteractRecordMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractRecord> getLinksusInteractRecordList(){
		return linksusInteractRecordMapper.getLinksusInteractRecordList();
	}

	/** ������ѯ */
	public LinksusInteractRecord getLinksusInteractRecordById(Long pid){
		return linksusInteractRecordMapper.getLinksusInteractRecordById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.insertLinksusInteractRecord(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.updateLinksusInteractRecord(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractRecordById(Long pid){
		return linksusInteractRecordMapper.deleteLinksusInteractRecordById(pid);
	}

	/** �ж�΢����¼�����Ƿ��л�����¼ */
	public LinksusInteractRecord checkIsExsitRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.checkIsExsitRecord(entity);
	}

	/** �����������¼�¼ʱ�估�û����»�����ϢID */
	public Integer updateLinksusInteractRecordById(Map params){
		return linksusInteractRecordMapper.updateLinksusInteractRecordById(params);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusInteractRecord entity = (LinksusInteractRecord) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession
							.update("com.linksus.dao.LinksusInteractRecordMapper.updateLinksusInteractRecord", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}