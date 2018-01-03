package com.linksus.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.LogUtil;
import com.linksus.common.util.ZLibUtil;

/**
 * 编码器/加压:4字节数据长度+对象字节数据
 * @author zhangew
 *
 */
public class MyBufferEncoder extends ProtocolEncoderAdapter{

	protected static final Logger logger = LoggerFactory.getLogger(MyBufferEncoder.class);

	public void encode(IoSession session,Object message,ProtocolEncoderOutput out) throws Exception{
		TransferEntity entity = (TransferEntity) message;
		byte[] compObjBytes = null;
		try{
			byte[] objBytes = ZLibUtil.ObjectToByte(entity);
			//logger.debug(">>>>>>>>>>>>>>>>>>压缩前字节长度:\t{}" , objBytes.length);
			compObjBytes = ZLibUtil.compress(objBytes);
			//logger.debug(">>>>>>>>>>>>>>>>>>压缩后字节长度:\t{}" , compObjBytes.length);
			int capacity = compObjBytes.length + 4;
			IoBuffer buffer = IoBuffer.allocate(capacity, false);
			buffer.setAutoExpand(true);
			buffer.putInt(compObjBytes.length);
			buffer.put(compObjBytes);
			buffer.flip();
			out.write(buffer);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			if(entity != null){
				logger.error("Encoder出现异常:{}-{}", entity.getTaskType(), entity.getTransferData());
			}else{
				logger.error("Encoder出现异常:entity is null");
			}
		}
	}
}
