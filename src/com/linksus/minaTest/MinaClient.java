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
 * IOService�ӿ�
 * �����������ǵĿͻ��˺ͷ���˵Ľӿڣ������� connector��Acceptor
 * IOproceser���̻߳������������ǵ�������������
 * ioFilter�ṩ���ݵĹ��˹���������������� ��־����Ϣ�Ĺ���
 * Handle �������ǵ�ҵ����� �Զ����Handle��Ҫʵ��IoHandleAcceptor
 * */
/**
 * �����ӺͶ�����
 * �����ӣ�ͨ��˫�����ڵı���һ������״̬���Ͽ���qq��
 *    ����½qq��ʱ�����Ǿ�������Ѷ��������һ���������Ӻ󣬾Ͳ��Ͽ������Ƿ����쳣��
 *    �ŵ㣺ռ����Դ
 * �����ӣ�ͨ��˫�������ֳ��ڵ�����״̬��httpЭ�飻
 *    ���ͻ��˷���http���󣬷���������http���󣬵�������ɣ����ؿͻ������ݺ�Ͽ����ӣ�
 *    
 * @param args
 */
/**
 * ����
 *    �����Ӹ�Ϊ������
 * @param args
 */
/**
 * 1��IOService: ʵ���˶�����ͨ�ŵĿͻ��˺ͷ����֮��ĳ���
 * ���������ͻ��˵��ӽӿ���IOConnector��
 * ������������˵��ӽӿ���IOAcceptor
 * 2.IOService���ã� ��������ͨ�ŵĿͻ��˺ͷ����
 * ���Թ�������˫���ĻỰsession��ͬʱ������ӹ�����
 * 3.IOService����ṹ
 *   ͨ����չ�ӽӿںͳ��������ﵽ����չ��Ŀ��
 *                IOService   
 *   IOAcceptpr                IOConnector
 *                abstractIoService
 *   abstractIOAcceptor        abstractIOConnector
 *  4.
 *   1.���Api 
 *   ��1��getFilterChain ��ù�������
 *   ��2��setHandler ����ҵ��handle
 *   ��3��getSessionConfig ��ûỰ��������Ϣ
 *   ��4��dispose �ر����������õķ���
 *   2.IOConnector
 *    ��1��connect ������������
 *    ��2��setConnectTimeout ���ӳ�ʱ����
 *   3.IOAcceptor 
 *    ��1��bind �󶨶˿�
 *    ��2��getLocalAddress ��ȡ����ip��ַ
 *   4.NioSocketAccptor
 *    ��1��accept ����һ������
 *    ��2��open ��һ��socketchannel
 *    ��3��select() ��ȡѡ����
 * IOFilter ��Ӧ�ó���������̣���ʵ���Ƕ��������ݺͶ���֮����໥ת��
 *          ����Ӧ�Ľ���ͱ���������Ҳ�ǹ�������һ�֣����ǶԹ�����Ҳ��������־����Ϣȷ�ϵȹ��ܡ�
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
    //���ñ�����������
    connector.getFilterChain().addLast("codec",
     new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
    		 LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue()))
    		);
    connector.setHandler(new MyClientHandle());
    ConnectFuture future = connector.connect(new InetSocketAddress(host,port));
	future.awaitUninterruptibly();
	ioSession =future.getSession();
	ioSession.write("���");
	ioSession.getCloseFuture().awaitUninterruptibly();
	connector.dispose();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}
