package com.linksus.test;

import java.util.ArrayList;
import java.util.List;

public class TestpIC{

	public static void main(String[] args){
		List<String> listPic = new ArrayList<String>();
		String firstPic = "http://app.qpic.cn/mblogpic/33b4a7f628b9a9b034c0";
		String secondPic = "http://app.qpic.cn/mblogpic/1cbe2f0580470aeb371e";
		String originalUrl = "";
		String middleUrl = "";
		String thumbUrl = "";
		listPic.add(firstPic);
		listPic.add(secondPic);
		if(listPic != null && listPic.size() > 0){
			for(int i = 0; i < listPic.size(); i++){
				String imagePic = listPic.get(i);
				originalUrl += imagePic + "/2000|";
				middleUrl += imagePic + "/460|";
				thumbUrl += imagePic + "/120|";
			}
		}
		System.out.println("+++++++++++" + originalUrl.substring(0, originalUrl.length() - 1));
		System.out.println("+++++++++++" + middleUrl);
		System.out.println("+++++++++++" + thumbUrl);
	}
}
