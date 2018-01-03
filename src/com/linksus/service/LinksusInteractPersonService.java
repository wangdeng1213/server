package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.InteractListQueryObj;
import com.linksus.entity.LinksusInteractPerson;

public interface LinksusInteractPersonService{

	/** 查询列表 */
	public List<LinksusInteractPerson> getLinksusInteractPersonList();

	/** 主键查询 */
	public LinksusInteractPerson getLinksusInteractPersonById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractPerson(LinksusInteractPerson entity);

	/** 更新 */
	//public Integer updateLinksusInteractPerson(LinksusInteractPerson entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractPersonById(Long pid);

	/** 检查数据是否存在互动人员表中 */
	public LinksusInteractPerson checkDataIsExsitInteractPerson(LinksusInteractPerson entity);

	/** 查询互动列表数据 */
	public List queryInteractDataList(InteractListQueryObj queryObj);

	/** 查询互动列表数据 */
	public Integer queryInteractDataListCount(InteractListQueryObj queryObj);

	public Map queryInteractDataList2(Map map);

	/**新互动列表查询*/
	public List newQueryInteractDataList(InteractListQueryObj queryObj);
}
