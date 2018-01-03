package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusTaskInteractGovMidMapper;
import com.linksus.entity.LinksusTaskInteractGovMid;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusTaskInteractGovMidService;

@Service("linksusTaskInteractGovMidService")
public class LinksusTaskInteractGovMidServiceImpl extends BaseService implements LinksusTaskInteractGovMidService{

	@Autowired
	private LinksusTaskInteractGovMidMapper linksusTaskInteractGovMidMapper;

	/** 查询列表 */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList(){
		return linksusTaskInteractGovMidMapper.getLinksusTaskInteractGovMidList();
	}

	/** 主键查询 */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid govMid){
		return linksusTaskInteractGovMidMapper.getLinksusTaskInteractGovMidById(govMid);
	}

	/** 新增 */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity){
		return linksusTaskInteractGovMidMapper.insertLinksusTaskInteractGovMid(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity){
		return linksusTaskInteractGovMidMapper.updateLinksusTaskInteractGovMid(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid){
		return linksusTaskInteractGovMidMapper.deleteLinksusTaskInteractGovMidById(pid);
	}

	/**根据类型和账号查询出临时表中互动的数据*/
	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map){
		return linksusTaskInteractGovMidMapper.getGovMidTempListByMap(map);
	}

	/** 清除临时表中数据  */
	public void deleteLinksusTaskInteractGovMid(){
		linksusTaskInteractGovMidMapper.deleteLinksusTaskInteractGovMid();
	}

	/**根据MID查询*/
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidListByMid(Long mid){
		List<LinksusTaskInteractGovMid> list = linksusTaskInteractGovMidMapper
				.getLinksusTaskInteractGovMidListByMid(mid);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid){
		return linksusTaskInteractGovMidMapper.deleteLinkSusTaskInteractGovMidByMid(mid);
	}

	@Override
	public List<Long> getDuoWenInteractGovMid(){
		return linksusTaskInteractGovMidMapper.getDuoWenInteractGovMid();
	}

	@Override
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList(){
		return linksusTaskInteractGovMidMapper.getLinksusCommentAndRelayGovMidList();
	}

	@Override
	public void updateInteractType(LinksusTaskInteractGovMid govMid){
		linksusTaskInteractGovMidMapper.updateInteractType(govMid);
	}

	@Override
	public Integer saveLinksusInteractGovMid(List<LinksusTaskInteractGovMid> list,String operType){
		saveEntity(list, operType);
		return 0;
	}

	@Override
	public void saveEntity(List dataList,String operType){
		for(int i = 0; i < dataList.size(); i++){
			LinksusTaskInteractGovMid entity = (LinksusTaskInteractGovMid) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.insert("com.linksus.dao.LinksusTaskInteractGovMidMapper.insertLinksusTaskInteractGovMid",
						entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusTaskInteractGovMidMapper.updateLinksusTaskInteractGovMid",
						entity);
			}
		}
	}
}