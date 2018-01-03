package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.linksus.task.TaskPersonDataInfoImport;

public class TaskPersonDataInfoImportInterface extends BaseInterface{

	// 导入用户数据xls
	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		//		Map paramap = new HashMap();
		//			String idType = paramsMap.get("idType").toString();
		//			String filename = paramsMap.get("filename").toString();
		//			filename = "d:\\导入模板.xlsx";
		//			String institution_id = paramsMap.get("institution_id").toString();
		//			BufferedInputStream bufferedInputStream = (BufferedInputStream) paramsMap
		//					.get("inputstream");
		//
		//			paramap.put("filename", filename);
		//			paramap.put("institution_id", institution_id);
		//			paramap.put("inputstream", bufferedInputStream);

		TaskPersonDataInfoImport task = new TaskPersonDataInfoImport(paramsMap);
		String rs = task.start(paramsMap);
		rsMap.put("reslut", rs);
		return rsMap;
	}
}
