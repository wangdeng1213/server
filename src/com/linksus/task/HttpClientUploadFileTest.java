package com.linksus.task;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**  
16. * @author wb-gaoy  
17. * @version $Id: HttpClientTest.java,v 0.1 2012-4-6 下午1:38:53 wb-gaoy Exp $  
1 */
public class HttpClientUploadFileTest{

	public void uploadFile(File file,String url){
		if(!file.exists()){
			return;
		}
		PostMethod postMethod = new PostMethod(url);
		try{
			//FilePart：用来上传文件的类   
			FilePart fp = new FilePart("filedata", file);
			StringPart accessToken = new StringPart(
					"access_token",
					"vQkOtoIHijZqglKrw6M8ARTvZzMgFLdal5aztupPpmUHIQVgv7cEYUvMrvclDgU_H84i0WNNbDM4iFCrpH4f4DPYX9jJkL5LE8Rd2LsD7x3T8drEQMceplp8RKbOWMnDpgBWtxvHzB_f3fY5OCn1VA");
			StringPart type = new StringPart("type", "voice");
			Part[] parts = { fp, accessToken, type };

			//对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装   
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 设置连接时间   
			client.executeMethod(postMethod);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			//释放连接   
			postMethod.releaseConnection();
		}
	}

	/**  
	 * @param args  
	 */
	public static void main(String[] args){
		HttpClientUploadFileTest test = new HttpClientUploadFileTest();
		test.uploadFile(new File("D:/test/ces.mp3"), "http://file.api.weixin.qq.com/cgi-bin/media/upload?");
	}
}
