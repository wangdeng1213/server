package com.linksus.common.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;

public class JsonUtil{

	private static ObjectMapper mapper;

	public static void main(String[] args){
		//Date bb = new Date();
	}

	private JsonUtil() {
	};

	static{
		if(mapper == null){
			mapper = new ObjectMapper();
			mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			mapper.getDeserializationConfig().set(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			//mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
			//  mapper.configure(DeserializationConfig.Feature., false); 
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mapper.getDeserializationConfig().setDateFormat(aa);
			mapper.getSerializationConfig().setDateFormat(aa);
		}
	}

	public static Object jsonToBean(String json,Class<?> cls) throws Exception{
		Object vo = null;
		vo = mapper.readValue(json, cls);
		return vo;
	}

	public static String list2json(List<?> list){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if(list != null && list.size() > 0){
			for(Object obj : list){
				try{
					json.append(beanToJson(obj));
				}catch (Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		}else{
			json.append("]");
		}
		return json.toString();
	}

	public static int i = 0;
	public static int m = 0;
	public static int sum = 0;

	public static Object json2Bean(String json,Class<?> cls){
		Object vo = null;
		i++;
		try{
			vo = mapper.readValue(json, cls);
		}catch (JsonParseException e){
			// TODO Auto-generated catch block
			//	e.printStackTrace();
			m++;
		}catch (JsonMappingException e){
			// TODO Auto-generated catch block
			//e.printStackTrace();
			sum++;
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vo;
	}

	public static String beanToJson(Object obj) throws Exception{
		return mapper.writeValueAsString(obj);

	}

	public static String bean2Json(Object obj){
		try{
			return mapper.writeValueAsString(obj);
		}catch (JsonGenerationException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JsonMappingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static List<?> json2list(String json,Class<?> elementClasses){
		JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, elementClasses);
		List<?> lst = null;
		try{
			lst = (List<?>) mapper.readValue(json, javaType);
		}catch (JsonParseException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JsonMappingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lst;
	}

	public static Map json2map(String json,Class<?> elementClasses){
		Map map = null;
		try{
			map = mapper.readValue(json, Map.class);
		}catch (JsonParseException e){
			e.printStackTrace();
		}catch (JsonMappingException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return map;
	}

	public static String getNodeByName(String json,String name){
		String textValue = null;
		try{
			JsonNode node = mapper.readTree(json);
			JsonNode value = node.get(name);
			if(value != null){
				textValue = value.toString();
			}
		}catch (JsonProcessingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textValue;

	}

	public static String getNodeValueByName(String json,String name){
		String textValue = null;
		try{
			JsonNode node = mapper.readTree(json);
			JsonNode value = node.get(name);
			if(value != null){
				textValue = value.toString();
				if(textValue.startsWith("\"") && textValue.endsWith("\"")){
					textValue = textValue.substring(1, textValue.length() - 1);
				}
			}
		}catch (JsonProcessingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textValue;

	}

	/** 
	 * @param s 参数 
	 * @return String 
	 */
	public static String string2json(String s){
		if(null == s){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++){
			char ch = s.charAt(i);
			switch (ch) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if(ch >= '\u0000' && ch <= '\u001F'){
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for(int k = 0; k < 4 - ss.length(); k++){
							sb.append('0');
						}
						sb.append(ss.toUpperCase());
					}else{
						sb.append(ch);
					}
			}
		}
		return sb.toString();
	}

	/** 
	 * @param map map对象 
	 * @return String 
	 */
	public static String map2json(Map<?, ?> map){
		StringBuilder json = new StringBuilder();
		json.append("{");
		if(map != null && map.size() > 0){
			for(Object key : map.keySet()){
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		}else{
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 返回回复文本XML格式字符串
	 * @param map map对象 
	 * @return String  
	 */
	public static String mapTextToXML(Map<?, ?> map){
		StringBuilder xmlContent = new StringBuilder();
		if(map != null && map.size() > 0){
			xmlContent.append("<xml>");
			for(Object key : map.keySet()){
				xmlContent.append("<" + key.toString() + ">");
				if(key.toString().equals("CreateTime")){
					xmlContent.append(map.get(key));
				}else{
					xmlContent.append("<![CDATA[" + map.get(key) + "]]>");
				}
				xmlContent.append("</" + key.toString() + ">");
			}
			xmlContent.append("</xml>");
		}
		return xmlContent.toString();
	}

	/**
	 * 返回回复图文XML格式字符串
	 * @param map map对象 
	 * @return String  
	 */
	public static String mapImageToXML(Map<?, ?> map){
		StringBuilder xmlContent = new StringBuilder();
		if(map != null && map.size() > 0){
			for(Object key : map.keySet()){
				xmlContent.append("<" + key.toString() + ">");
				if(key.toString().equals("ArticleCount") || key.toString().equals("CreateTime")){
					xmlContent.append(map.get(key));
				}else{
					xmlContent.append("<![CDATA[" + map.get(key) + "]]>");
				}
				xmlContent.append("</" + key.toString() + ">");
			}
		}
		return xmlContent.toString();
	}

	/**
	 * 返回回复图文内容XML格式字符串
	 * @param map map对象 
	 * @return String  
	 */
	public static String mapImageContentToXML(List<Map<?, ?>> contents){
		StringBuilder xmlContent = new StringBuilder();
		if(contents != null && contents.size() > 0){
			xmlContent.append("<Articles>");
			for(Map<?, ?> map : contents){
				if(map != null && map.size() > 0){
					xmlContent.append("<item>");
					for(Object key : map.keySet()){
						xmlContent.append("<" + key.toString() + ">");
						if(key.toString().equals("ArticleCount") || key.toString().equals("CreateTime")){
							xmlContent.append(map.get(key));
						}else{
							xmlContent.append("<![CDATA[" + map.get(key) + "]]>");
						}
						xmlContent.append("</" + key.toString() + ">");
					}
					xmlContent.append("</item>");
				}
			}
			xmlContent.append("</Articles>");
		}
		return xmlContent.toString();
	}

	/** 
	 * @param obj 任意对象 
	 * @return String 
	 */
	public static String object2json(Object obj){
		StringBuilder json = new StringBuilder();
		if(obj == null){
			json.append("\"\"");
		}else if(obj instanceof String || obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte){
			json.append("\"").append(string2json(obj.toString())).append("\"");
		}else if(obj instanceof Object[]){
			json.append(array2json((Object[]) obj));
		}else if(obj instanceof List){
			json.append(list2json((List<?>) obj));
		}else if(obj instanceof Map){
			json.append(map2json((Map<?, ?>) obj));
		}else if(obj instanceof Set){
			json.append(set2json((Set<?>) obj));
		}else{
			json.append(bean2Json(obj));
		}
		return json.toString();
	}

	/** 
	 * @param array 对象数组 
	 * @return String 
	 */
	public static String array2json(Object[] array){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if(array != null && array.length > 0){
			for(Object obj : array){
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		}else{
			json.append("]");
		}
		return json.toString();
	}

	/** 
	 * @param set 集合对象 
	 * @return String 
	 */
	public static String set2json(Set<?> set){
		StringBuilder json = new StringBuilder();
		json.append("[");
		if(set != null && set.size() > 0){
			for(Object obj : set){
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		}else{
			json.append("]");
		}
		return json.toString();
	}

	/** 
	 * @param json 集合对象 
	 * @return set 
	 */
	public static Set<?> json2set(String json,Class<?> elementClasses){
		JavaType javaType = mapper.getTypeFactory().constructParametricType(HashSet.class, elementClasses);
		Set<?> lst = null;
		try{
			lst = (Set<?>) mapper.readValue(json, javaType);
		}catch (JsonParseException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JsonMappingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lst;
	}
}
