package com.linksus.interfaces;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.InterfaceConfig;
import com.linksus.common.config.InterfaceParams;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;

/**
 * �ͻ���ȡ����������������
 * 
 * @author zhangew
 * 
 */
@SuppressWarnings("serial")
public class InterfaceServlet extends HttpServlet{

	protected static final Logger logger = LoggerFactory.getLogger(InterfaceServlet.class);

	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		resp.setContentType("text/html;charset=UTF-8");
		printParameters(req, "get");
		action(req, resp);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		printParameters(req, "post");
		action(req, resp);
	}

	/**
	 * �ⲿ���ýӿ�
	 * 
	 * @param req
	 * @param response
	 * @throws IOException
	 * @throws CacheException
	 */
	private void action(HttpServletRequest req,HttpServletResponse response) throws IOException{
		String taskType = req.getParameter("taskType");
		if(StringUtils.isBlank(taskType)){
			response.getWriter().print(StringUtil.getHttpResultErrorStr("10001"));
			return;
		}
		try{
			Map map = LoadConfig.getInterfaceMap();
			InterfaceConfig config = (InterfaceConfig) map.get(taskType);
			if(config == null){//û�иýӿ�
				response.getWriter().print(StringUtil.getHttpResultErrorStr("10013"));
				return;
			}else{//������url����
				if(!"1".equals(config.getStatus())){
					throw new Exception("�ӿ�" + config.getTaskType() + "-" + config.getTaskName() + ":δ����,���޸�����");
				}
				List paramList = config.getParamsList();
				Map params = new HashMap();
				StringBuffer buffer = new StringBuffer("#����");
				boolean paramError = false;
				for(int i = 0; i < paramList.size(); i++){
					InterfaceParams interfaceParams = (InterfaceParams) paramList.get(i);
					String parameter = req.getParameter(interfaceParams.getName());
					if("1".equals(interfaceParams.getRequired())){//����
						if(StringUtils.isBlank(parameter)){//��ֵ
							paramError = true;
							buffer.append("[").append(interfaceParams.getName()).append("-").append(
									interfaceParams.getParamDisp()).append("]");
						}else{
							//����ת��
							Object o;
							try{
								o = castType(parameter, interfaceParams.getDataType());
							}catch (Exception e){
								buffer.setLength(0);
								buffer.append("����[").append(interfaceParams.getName()).append("-").append(
										interfaceParams.getParamDisp()).append("]���Ͳ�ƥ��:").append(
										interfaceParams.getDataType());
								response.getWriter().print(StringUtil.getHttpResultErrorStr(buffer.toString()));
								return;
							}
							params.put(interfaceParams.getName(), o);
						}
					}else{
						//����ת��
						Object o;
						try{
							o = castType(parameter, interfaceParams.getDataType());
						}catch (Exception e){
							buffer.setLength(0);
							buffer.append("����[").append(interfaceParams.getName()).append("-").append(
									interfaceParams.getParamDisp()).append("]���Ͳ�ƥ��:").append(
									interfaceParams.getDataType());
							response.getWriter().print(StringUtil.getHttpResultErrorStr(buffer.toString()));
							return;
						}
						params.put(interfaceParams.getName(), o);
					}
				}
				if(paramError){
					buffer.append("����Ϊ��");
					response.getWriter().print(StringUtil.getHttpResultErrorStr(buffer.toString()));
					return;
				}else{//���ýӿ�
					BaseInterface task = InterfaceFactory.getInstance(taskType);
					String rs = task.exec(params);
					response.getWriter().print(rs);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.saveException(logger, e);
			response.getWriter().print(StringUtil.getHttpResultForException(e));
		}
	}

	/**
	 * ��־����ӿڵ��ò��� ���ڵ���
	 * @param request
	 * @param reqType
	 * @throws IOException
	 */
	private void printParameters(HttpServletRequest request,String reqType) throws IOException{
		String ipAddr = HttpUtil.getIpAddr(request);
		String taskType = request.getParameter("taskType");
		if("get".equals(reqType)){
			logger.info(">>>>>>>>>>>>>>>>>>>IP��ַ:{}���ýӿ�(get):{}--{}?{}", ipAddr, taskType, request.getRequestURL(),
					request.getQueryString());
		}else if("post".equals(reqType)){
			Enumeration<String> e = request.getParameterNames();
			String parameterName, parameterValue;
			StringBuffer buff = new StringBuffer();
			while (e.hasMoreElements()){
				parameterName = e.nextElement();
				parameterValue = request.getParameter(parameterName);
				buff.append(parameterName).append("=").append(parameterValue).append("&");
			}
			if(buff.length() > 0){
				buff.deleteCharAt(buff.length() - 1);
			}
			if(!"26".equals(taskType)){
				logger.info(">>>>>>>>>>>>>>>>>>>IP��ַ:{}���ýӿ�(post):{}--{}?{}", ipAddr, taskType,
						request.getRequestURL(), buff.toString());
			}
		}
	}

	/**
	 * ��������ת����������
	 * @param parameter
	 * @param dataType
	 * @return
	 */
	private Object castType(String parameter,String dataType){
		Object obj = null;
		if(StringUtils.isBlank(parameter)){
			return null;
		}
		if(StringUtils.isBlank(dataType)){
			return parameter;
		}
		if(dataType.toUpperCase().equals("LONG")){
			obj = new Long(parameter);
		}else if(dataType.toUpperCase().equals("INTEGER")){
			obj = new Integer(parameter);
		}else{
			return parameter;
		}
		return obj;
	}
}
