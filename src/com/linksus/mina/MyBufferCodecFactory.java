package com.linksus.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyBufferCodecFactory implements ProtocolCodecFactory{

	private final MyBufferEncoder encoder;
	private final MyBufferDecoder decoder;

	public MyBufferCodecFactory() {
		encoder = new MyBufferEncoder();
		decoder = new MyBufferDecoder();
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception{
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception{
		return encoder;
	}

}
