package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.AttentionRelationEntrityMapper;
import com.linksus.entity.AttentionRelationEntrity;
import com.linksus.service.AttentionRelationEntrityService;

@Service("attentionRelationEntrityService")
public class AttentionRelationEntrityServiceImpl implements AttentionRelationEntrityService{

	@Autowired
	private AttentionRelationEntrityMapper attentionRelationEntrityMapper;

	/** 根据找好类型查询列表 */
	public List<AttentionRelationEntrity> getAttentionRelationEntrityList(Map map){
		return attentionRelationEntrityMapper.getAttentionRelationEntrityList(map);
	}

	public List<AttentionRelationEntrity> getRelationAttentionTask(Map map){
		return attentionRelationEntrityMapper.getRelationAttentionTask(map);
	}

}