package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskException;

public interface LinksusTaskExceptionMapper{

	/** 列表查询 */
	public List<LinksusTaskException> getLinksusTaskExceptionList();

	/** 主键查询 */
	public LinksusTaskException getLinksusTaskExceptionById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskException(LinksusTaskException entity);

	/** 更新 */
	public Integer updateLinksusTaskException(LinksusTaskException entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskExceptionById(Long pid);

}
