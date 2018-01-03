package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractPersonMapper;
import com.linksus.entity.InteractListQueryObj;
import com.linksus.entity.LinksusInteractPerson;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractPersonService;

@Service("linksusInteractPersonService")
public class LinksusInteractPersonServiceImpl extends BaseService implements LinksusInteractPersonService{

	@Autowired
	private LinksusInteractPersonMapper linksusInteractPersonMapper;

	/** 查询列表 */
	public List<LinksusInteractPerson> getLinksusInteractPersonList(){
		return linksusInteractPersonMapper.getLinksusInteractPersonList();
	}

	/** 主键查询 */
	public LinksusInteractPerson getLinksusInteractPersonById(Long pid){
		return linksusInteractPersonMapper.getLinksusInteractPersonById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractPerson(LinksusInteractPerson entity){
		return linksusInteractPersonMapper.insertLinksusInteractPerson(entity);
	}

	/** 更新 */
	//	public Integer updateLinksusInteractPerson(LinksusInteractPerson entity){
	//		return linksusInteractPersonMapper.updateLinksusInteractPerson(entity);
	//	}

	/** 主键删除 */
	public Integer deleteLinksusInteractPersonById(Long pid){
		return linksusInteractPersonMapper.deleteLinksusInteractPersonById(pid);
	}

	/** 检查数据是否存在互动人员表中 */
	public LinksusInteractPerson checkDataIsExsitInteractPerson(LinksusInteractPerson entity){
		return linksusInteractPersonMapper.checkDataIsExsitInteractPerson(entity);

	}

	/** 查询互动列表*/
	public List queryInteractDataList(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.queryInteractDataList(queryObj);
	}

	/** 查询互动列表总数*/
	public Integer queryInteractDataListCount(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.queryInteractDataListCount(queryObj);
	}

	@Override
	public Map queryInteractDataList2(Map map){
		return linksusInteractPersonMapper.queryInteractDataList2(map);
	}

	/**新互动列表查询*/
	public List newQueryInteractDataList(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.newQueryInteractDataList(queryObj);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		for(int i = 0; i < dataList.size(); i++){
			LinksusInteractPerson entity = (LinksusInteractPerson) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusInteractPersonMapper.updateLinksusInteractPerson", entity);
			}
		}
	}
}