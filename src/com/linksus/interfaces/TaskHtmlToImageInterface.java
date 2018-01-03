package com.linksus.interfaces;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.FileUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.ShellUtil;
import com.linksus.common.util.StringUtil;

/**
 * 将html字符串转换为图片上传,返回url
 * @author zhangew
 *
 */
public class TaskHtmlToImageInterface extends BaseInterface{

	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		String htmlStr = (String) paramsMap.get("htmlstr");
		String urlStr = (String) paramsMap.get("urlstr");
		if(StringUtils.isBlank(htmlStr) && StringUtils.isBlank(urlStr)){
			rsMap.put("errcode", "20002");
			return rsMap;
		}
		String fileType = "jpg";
		if(!StringUtils.isBlank(htmlStr)){
			fileType = "png";
		}
		String filePath = LoadConfig.getString("tempFilePath");
		String fileName = StringUtil.randomString(5) + "." + fileType;
		String tmpFileName = filePath + File.separator + "tmp_" + fileName;
		//String tmpZoonName=filePath+File.separator+"zoon_"+fileName;
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		if(!StringUtils.isBlank(htmlStr)){
			imageGenerator.loadHtml(htmlStr);
			imageGenerator.saveAsImage(tmpFileName);
		}else{//url
			Map tmpMap = ShellUtil.urlToImage(urlStr, tmpFileName);
			String execFlag = (String) tmpMap.get("execFlag");
			if(!"0".equals(execFlag)){
				rsMap.put("errcode", "#shell执行出现错误,错误号" + execFlag);
				return rsMap;
			}
		}

		File tempFile = new File(tmpFileName);
		//File zoonFile=new File(tmpZoonName);
		Map paramMap = new HashMap();
		//ImageUtil.zoomImage(tempFile, zoonFile, fileType, 200, 200);
		//paramMap.put("thumb."+fileType, FileUtil.getBase64StrFormFile(zoonFile));
		paramMap.put("original." + fileType, FileUtil.getBase64StrFormFile(tempFile));
		//logger.debug("----------------->before send to server original.{}--{}",fileType,FileUtil.getBase64StrFormFile(tempFile));
		logger.debug("----------------->before send to server");
		String rsStr = HttpUtil.sendToDataServer(paramMap);
		String image = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_" + fileType);
		logger.debug("----------------->after send to server:{}", rsStr);
		//String thumb=JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"),"thumb_"+fileType);
		//imgUrl=JsonUtil.object2json(image).replaceAll("\\\\/", "/");
		rsMap.put("picurl", image);
		return rsMap;
	}
}
