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

	/** 查询列表 */
	public List<LinksusInteractRecord> getLinksusInteractRecordList(){
		return linksusInteractRecordMapper.getLinksusInteractRecordList();
	}

	/** 主键查询 */
	public LinksusInteractRecord getLinksusInteractRecordById(Long pid){
		return linksusInteractRecordMapper.getLinksusInteractRecordById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.insertLinksusInteractRecord(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.updateLinksusInteractRecord(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractRecordById(Long pid){
		return linksusInteractRecordMapper.deleteLinksusInteractRecordById(pid);
	}

	/** 判断微博记录表中是否有互动记录 */
	public LinksusInteractRecord checkIsExsitRecord(LinksusInteractRecord entity){
		return linksusInteractRecordMapper.checkIsExsitRecord(entity);
	}

	/** 根据主键更新记录时间及用户最新互动消息ID */
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