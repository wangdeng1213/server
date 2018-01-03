package com.linksus.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.LogUtil;
import com.linksus.common.util.ZLibUtil;

/**
 * 解码器/解压:字节码转换为对象 无需处理粘包/丢包
 * 
 * @author zhangew
 * 
 */
public class MyBufferDecoder extends CumulativeProtocolDecoder{

	protected static final Logger logger = LoggerFactory.getLogger(MyBufferDecoder.class);
	private static final String CONTEXT = MyBufferDecoder.class.getName() + ".STATE";
	private static final int PACK_HEAD_LENGTH = 4;

	@Override
	protected boolean doDecode(IoSession session,IoBuffer in,ProtocolDecoderOutput out) throws Exception{
		try{
			Context ctx = getContext(session);
			// 先把当前buffer中的数据追加到Context的buffer当中
			ctx.append(in);
			// 把position指向0位置，把limit指向原来的position位置
			IoBuffer buf = ctx.getBuffer();
			buf.flip();
			while (buf.remaining() >= PACK_HEAD_LENGTH){
				buf.mark();
				// 设置总长度
				if(ctx.getMsgLength() <= 0){
					// 读取消息头部分
					//byte[] bLeng = new byte[PACK_HEAD_LENGTH];
					//buf.get(bLeng);
					int length = -1;
					try{
						length = buf.getInt();
						//length = Integer.parseInt(new String(bLeng));
					}catch (NumberFormatException ex){
						ex.printStackTrace();
					}
					if(length > 0){
						ctx.setMsgLength(length);
					}
				}

				// 读取消息头部分
				int length = ctx.getMsgLength();
				// 检查读取的包头是否正常，不正常的话清空buffer
				if(length <= 0){ // || length > maxPackLength2) {
					buf.clear();
					LogUtil.saveException(logger, new Exception("包头解析存在错误,忽略数据,请查找原因!"));
					break;
					// 读取正常的消息包，并写入输出流中，以便IoHandler进行处理
				}else if(length > PACK_HEAD_LENGTH && buf.remaining() >= length){
					// 完整的数据读取之后，就可以开始做你自己想做的操作了
					parseData(buf, length, out);
					ctx.setMsgLength(0);
				}else{
					// 如果消息包不完整
					// 将指针重新移动消息头的起始位置
					buf.reset();
					ctx.setMsgLength(0);
					break;
				}
			}
			if(buf.hasRemaining()){ // 如果有剩余的数据，则放入Session中
				// 将数据移到buffer的最前面
				IoBuffer temp = IoBuffer.allocate(2048).setAutoExpand(true);
				temp.put(buf);
				temp.flip();
				buf.clear();
				buf.put(temp);
			}else{ // 如果数据已经处理完毕，进行清空
				buf.clear();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		return true;
	}

	private void parseData(IoBuffer in,int length,ProtocolDecoderOutput out){
		byte[] compObjBytes = new byte[length];
		//logger.debug(">>>>>>>>>>>>>>>>>>解压前字节长度:\t{}", compObjBytes.length);
		in.get(compObjBytes);
		byte[] objBytes = ZLibUtil.decompress(compObjBytes);
		//logger.debug(">>>>>>>>>>>>>>>>>>解压后字节长度:\t{}" , objBytes.length);
		TransferEntity entity = (TransferEntity) ZLibUtil.ByteToObject(objBytes);
		out.write(entity);
	}

	/**
	 * @param session
	 *            会话信息
	 * @return 返回session中的累积
	 */
	private Context getContext(IoSession session){
		Context ctx = (Context) session.getAttribute(CONTEXT);
		if(ctx == null){
			ctx = new Context();
			session.setAttribute(CONTEXT, ctx);
		}
		return ctx;
	}

	private class Context{

		private IoBuffer buf;
		private int msgLength = 0;
		private int overflowPosition = 0;

		private Context() {
			buf = IoBuffer.allocate(2048).setAutoExpand(true);
		}

		public IoBuffer getBuffer(){
			return buf;
		}

		public int getOverflowPosition(){
			return overflowPosition;
		}

		public int getMsgLength(){
			return msgLength;
		}

		public void setMsgLength(int msgLength){
			this.msgLength = msgLength;
		}

		public void reset(){
			this.buf.clear();
			this.overflowPosition = 0;
			this.msgLength = 0;
		}

		public void append(IoBuffer in){
			getBuffer().put(in);
		}
	}
}
