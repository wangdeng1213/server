package com.linksus.minaTest;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {
private static String host ="127.0.0.1";
private static int port=7080;
/**
 * IOService接口
 * 用于描述我们的客户端和服务端的接口，其子类 connector和Acceptor
 * IOproceser多线程环境来处理我们的连接请求流程
 * ioFilter提供数据的过滤工作：包括编码解码 日志等信息的过滤
 * Handle 就是我们的业务对象 自定义的Handle需要实现IoHandleAcceptor
 * */
/**
 * 长连接和短连接
 * 长连接：通信双方长期的保持一个连接状态不断开如qq；
 *    当登陆qq的时候，我们就连接腾讯服务器，一旦简历连接后，就不断开，除非发生异常；
 *    优点：占用资源
 * 短连接：通信双方不保持长期的连接状态如http协议；
 *    当客户端发起http请求，服务器处理http请求，当处理完成，返回客户端数据后断开连接；
 *    
 * @param args
 */
/**
 * 改造
 *    长连接改为短连接
 * @param args
 */
/**
 * 1、IOService: 实现了对网络通信的客户端和服务端之间的抽象，
 * 用于描述客户端的子接口是IOConnector，
 * 用于描述服务端的子接口是IOAcceptor
 * 2.IOService作用： 管理网络通信的客户端和服务端
 * 可以管理连接双方的会话session，同时可以添加过滤器
 * 3.IOService的类结构
 *   通过扩展子接口和抽象的子类达到了扩展的目的
 *                IOService   
 *   IOAcceptpr                IOConnector
 *                abstractIoService
 *   abstractIOAcceptor        abstractIOConnector
 *  4.
 *   1.相关Api 
 *   （1）getFilterChain 获得过滤器链
 *   （2）setHandler 设置业务handle
 *   （3）getSessionConfig 获得会话的配置信息
 *   （4）dispose 关闭连接所调用的方法
 *   2.IOConnector
 *    （1）connect 发起连接请求
 *    （2）setConnectTimeout 连接超时设置
 *   3.IOAcceptor 
 *    （1）bind 绑定端口
 *    （2）getLocalAddress 获取本地ip地址
 *   4.NioSocketAccptor
 *    （1）accept 接受一个连接
 *    （2）open 打开一个socketchannel
 *    （3）select() 获取选择器
 * IOFilter 对应用程序和网络编程，其实就是二进制数据和对象之间的相互转化
 *          有相应的解码和编码器。这也是过滤器的一种，我们对过滤器也可以做日志。消息确认等功能。
 *          IOfilter
 *       IoFilterAdapter 
 *      ExecutorFilter 
 * IoFilter 
 * */
public static void main(String[] args) {
	try{
	IoSession ioSession =null;
	IoConnector connector = new NioSocketConnector();
    connector.setConnectTimeout(3000);
    //设置编码解码过滤器
    connector.getFilterChain().addLast("codec",
     new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
    		 LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue()))
    		);
    connector.setHandler(new MyClientHandle());
    ConnectFuture future = connector.connect(new InetSocketAddress(host,port));
	future.awaitUninterruptibly();
	ioSession =future.getSession();
	ioSession.write("你好");
	ioSession.getCloseFuture().awaitUninterruptibly();
	connector.dispose();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
