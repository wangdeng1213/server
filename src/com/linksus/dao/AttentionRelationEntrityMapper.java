package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.AttentionRelationEntrity;

public interface AttentionRelationEntrityMapper{

	/** ¡–±Ì≤È—Ø */
	public List<AttentionRelationEntrity> getAttentionRelationEntrityList(Map map);

	public List<AttentionRelationEntrity> getRelationAttentionTask(Map map);
}
