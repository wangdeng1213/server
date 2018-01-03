package com.linksus.minaTest;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {
static int port =7080;
static IoAcceptor accept=null;
/**
 * NIOSocket
 * 设置编码解码过滤器
 * 设置一些session的属性 休闲空闲的时间 缓冲区的大小
 * 绑定一个端口
 * */
public static void main(String[] args) {
	try{
	accept =new NioSocketAcceptor();
	accept.getFilterChain().addLast("codec", new ProtocolCodecFilter(
			new TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue())
			));
	accept.getSessionConfig().setReadBufferSize(1024);
	accept.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	accept.setHandler(new MyHandle());//设置session
	accept.bind(new InetSocketAddress(port));
	System.out.println("server开启");
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
