package com.linksus.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.entity.UrlEntity;

public class HttpTest{

	protected static final Log logger = LogFactory.getLog(HttpTest.class);

	public static void main(String[] args) throws Exception{

		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setUrlType(1);
		urlEntity.setLimitType(0);
		urlEntity.setUrl("https://open.t.qq.com/api/friends/fanslist_s?");

		Map map = new HashMap();
		map.put("oauth_version", "2.a");
		map.put("clientip", "114.249.178.225");
		map.put("oauth_consumer_key", "801378249");
		map.put("format", "json");
		map.put("reqnum", "200");
		map.put("startindex", "0");
		map.put("openid", "A8846582924473348120DE075E4E281A");
		map.put("access_token", "7668038114406a91748b47f123a4e547");
		String str = HttpUtil.getRequest(urlEntity, map);

		System.out.println("---" + str);
		String data = JsonUtil.getNodeByName(str, "data");
		String info = JsonUtil.getNodeByName(data, "info");
		List list = JsonUtil.json2list(info, Map.class);
		System.out.println(list.size());
	}
}
