package com.linksus.mina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.firewall.Subnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.StringUtil;

public class WhitelistFilter extends IoFilterAdapter{

	private final List<Subnet> whitelist = new CopyOnWriteArrayList<Subnet>();
	private final String configFilePath = "config/client-white-list.txt";
	private Set refusedIps = new HashSet();
	protected static final Logger logger = LoggerFactory.getLogger(WhitelistFilter.class);

	public WhitelistFilter() {
		super();
		//读取配置文件 添加IP白名单
		try{
			InputStream is = WhitelistFilter.class.getResourceAsStream("/" + configFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (true){
				String line = br.readLine();
				if(line == null){
					break;
				}
				if(StringUtil.ipCheck(line)){
					logger.debug("白名单ip地址:{}", line);
					Subnet subnet = new Subnet(InetAddress.getByName(line), 32);
					whitelist.add(subnet);
				}else{
					logger.error("过滤掉无效的ip地址:{}", line);
				}
			}
			br.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public Set getRefusedIps(){
		return refusedIps;
	}

	public void setWhitelist(InetAddress[] addresses){
		if(addresses == null){
			throw new NullPointerException("addresses");
		}
		whitelist.clear();
		for(InetAddress addr : addresses){
			allow(addr);
		}
	}

	public void setSubnetWhitelist(Subnet[] subnets){
		if(subnets == null){
			throw new NullPointerException("Subnets must not be null");
		}
		whitelist.clear();
		for(Subnet subnet : subnets){
			allow(subnet);
		}
	}

	public void setWhitelist(Iterable<InetAddress> addresses){
		if(addresses == null){
			throw new NullPointerException("addresses");
		}

		whitelist.clear();

		for(InetAddress address : addresses){
			allow(address);
		}
	}

	public void setSubnetWhitelist(Iterable<Subnet> subnets){
		if(subnets == null){
			throw new NullPointerException("Subnets must not be null");
		}
		whitelist.clear();
		for(Subnet subnet : subnets){
			allow(subnet);
		}
	}

	public void allow(InetAddress address){
		if(address == null){
			throw new NullPointerException("Adress to block can not be null");
		}

		allow(new Subnet(address, 32));
	}

	public void allow(Subnet subnet){
		if(subnet == null){
			throw new NullPointerException("Subnet can not be null");
		}
		whitelist.add(subnet);
		updateWhiteListFile();
	}

	public void disallow(InetAddress address){
		if(address == null){
			throw new NullPointerException("Adress to unblock can not be null");
		}
		disallow(new Subnet(address, 32));
	}

	public void disallow(Subnet subnet){
		if(subnet == null){
			throw new NullPointerException("Subnet can not be null");
		}
		whitelist.remove(subnet);
		updateWhiteListFile();
	}

	@Override
	public void sessionCreated(NextFilter nextFilter,IoSession session){
		if(isAllowed(session)){
			nextFilter.sessionCreated(session);
		}else{
			blockSession(session);
		}
	}

	@Override
	public void sessionOpened(NextFilter nextFilter,IoSession session) throws Exception{
		if(isAllowed(session)){
			nextFilter.sessionOpened(session);
		}else{
			blockSession(session);
		}
	}

	@Override
	public void sessionClosed(NextFilter nextFilter,IoSession session) throws Exception{
		if(isAllowed(session)){
			nextFilter.sessionClosed(session);
		}else{
			blockSession(session);
		}
	}

	@Override
	public void sessionIdle(NextFilter nextFilter,IoSession session,IdleStatus status) throws Exception{
		if(isAllowed(session)){
			nextFilter.sessionIdle(session, status);
		}else{
			blockSession(session);
		}
	}

	@Override
	public void messageReceived(NextFilter nextFilter,IoSession session,Object message){
		if(isAllowed(session)){
			nextFilter.messageReceived(session, message);
		}else{
			blockSession(session);
		}
	}

	@Override
	public void messageSent(NextFilter nextFilter,IoSession session,WriteRequest writeRequest) throws Exception{
		if(isAllowed(session)){
			nextFilter.messageSent(session, writeRequest);
		}else{
			blockSession(session);
		}
	}

	private void blockSession(IoSession session){
		logger.warn("Remote address is not allowed; closing.");
		session.close(true);
	}

	private boolean isAllowed(IoSession session){
		SocketAddress remoteAddress = session.getRemoteAddress();
		//logger.info(">>>>>>>>>>>>addr:"+remoteAddress.toString()+"连接");
		if(remoteAddress instanceof InetSocketAddress){
			InetAddress address = ((InetSocketAddress) remoteAddress).getAddress();
			// check all subnets   
			for(Subnet subnet : whitelist){
				if(subnet.inSubnet(address)){
					return true;
				}
			}
			refusedIps.add(address.getHostAddress());
		}
		return false;
	}

	public List getCurrentWhiteList(){
		List list = new ArrayList();
		for(Subnet subnet : whitelist){
			String str = subnet.toString();
			String ipAddr = str.substring(0, str.indexOf("/"));
			list.add(ipAddr);
		}
		return list;
	}

	/**
	 * 更新配置文件
	 * @param set
	 */
	private synchronized void updateWhiteListFile(){
		try{
			URL file = WhitelistFilter.class.getClassLoader().getResource(configFilePath);
			String filePath = URLDecoder.decode(file.getFile(), "UTF-8");
			logger.debug("file:{}", filePath);
			PrintWriter pw = new PrintWriter(filePath);
			Set set = new HashSet();
			for(Subnet subnet : whitelist){
				String str = subnet.toString();
				String ipAddr = str.substring(0, str.indexOf("/"));
				if(set.contains(ipAddr)){
					continue;
				}
				set.add(ipAddr);
				pw.println(ipAddr);
			}
			pw.flush();
			pw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}