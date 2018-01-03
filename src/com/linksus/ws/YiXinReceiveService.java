package com.linksus.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "yiXinReceiveService")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public interface YiXinReceiveService{

	@WebMethod(operationName = "saveService", action = "urn:saveService")
	public String saveService(@WebParam(name = "serviceName") String serviceName,
			@WebParam(name = "xmlInfo") String xmlInfo);
}
