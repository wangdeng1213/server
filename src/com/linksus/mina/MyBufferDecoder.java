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
 * ������/��ѹ:�ֽ���ת��Ϊ���� ���账��ճ��/����
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
			// �Ȱѵ�ǰbuffer�е�����׷�ӵ�Context��buffer����
			ctx.append(in);
			// ��positionָ��0λ�ã���limitָ��ԭ����positionλ��
			IoBuffer buf = ctx.getBuffer();
			buf.flip();
			while (buf.remaining() >= PACK_HEAD_LENGTH){
				buf.mark();
				// �����ܳ���
				if(ctx.getMsgLength() <= 0){
					// ��ȡ��Ϣͷ����
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

				// ��ȡ��Ϣͷ����
				int length = ctx.getMsgLength();
				// ����ȡ�İ�ͷ�Ƿ��������������Ļ����buffer
				if(length <= 0){ // || length > maxPackLength2) {
					buf.clear();
					LogUtil.saveException(logger, new Exception("��ͷ�������ڴ���,��������,�����ԭ��!"));
					break;
					// ��ȡ��������Ϣ������д��������У��Ա�IoHandler���д���
				}else if(length > PACK_HEAD_LENGTH && buf.remaining() >= length){
					// ���������ݶ�ȡ֮�󣬾Ϳ��Կ�ʼ�����Լ������Ĳ�����
					parseData(buf, length, out);
					ctx.setMsgLength(0);
				}else{
					// �����Ϣ��������
					// ��ָ�������ƶ���Ϣͷ����ʼλ��
					buf.reset();
					ctx.setMsgLength(0);
					break;
				}
			}
			if(buf.hasRemaining()){ // �����ʣ������ݣ������Session��
				// �������Ƶ�buffer����ǰ��
				IoBuffer temp = IoBuffer.allocate(2048).setAutoExpand(true);
				temp.put(buf);
				temp.flip();
				buf.clear();
				buf.put(temp);
			}else{ // ��������Ѿ�������ϣ��������
				buf.clear();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		return true;
	}

	private void parseData(IoBuffer in,int length,ProtocolDecoderOutput out){
		byte[] compObjBytes = new byte[length];
		//logger.debug(">>>>>>>>>>>>>>>>>>��ѹǰ�ֽڳ���:\t{}", compObjBytes.length);
		in.get(compObjBytes);
		byte[] objBytes = ZLibUtil.decompress(compObjBytes);
		//logger.debug(">>>>>>>>>>>>>>>>>>��ѹ���ֽڳ���:\t{}" , objBytes.length);
		TransferEntity entity = (TransferEntity) ZLibUtil.ByteToObject(objBytes);
		out.write(entity);
	}

	/**
	 * @param session
	 *            �Ự��Ϣ
	 * @return ����session�е��ۻ�
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
