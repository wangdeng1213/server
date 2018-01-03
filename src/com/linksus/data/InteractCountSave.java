package com.linksus.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.service.LinksusRelationUserAccountService;

/**
 * 互动次数/互动类型更新
 * @author zhangew
 *
 */
public class InteractCountSave extends BaseDataSave{

	protected static final Logger logger = LoggerFactory.getLogger(InteractCountSave.class);

	LinksusRelationUserAccountService userAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");
	/** 上次提交时间 */
	private static long lastSyncTime = 0;
	/** 最大提交间隔时间 毫秒*/
	private static String hkey = "interact_count";
	private static String userhKey = "relation_user_account";

	/**
	 * 更新互动次数/互动类型
	 * @param obj
	 * @param operType
	 */
	public static void updateUserAcctInteractInfo(Long userId,Long accountId,int iType){
		String fieldKey = accountId.toString() + "#" + userId.toString();
		RedisUtil.setRedisHashIncrby(hkey, fieldKey, 1);
		String strResult = RedisUtil.getRedisHash(userhKey, fieldKey);
		String[] str = strResult.split("#");
		String interactType = str[1];
		String start = interactType.substring(0, iType - 1);
		String end = interactType.substring(iType);
		String interactTypeStr = start + "1" + end;
		RedisUtil.setRedisHash(userhKey, fieldKey, str[0] + "#" + interactTypeStr);
	}

	private long getIntervalTime(long lastTime){
		long currTime = new Date().getTime();
		return currTime - lastTime;
	}

	@Override
	public void run(){
		String hkey = "interact_count";
		while (true){
			long userAcctcommitInterval = Long.parseLong(LoadConfig.getString("userAcctcommitInterval"));
			try{
				long interval = userAcctcommitInterval - getIntervalTime(lastSyncTime);
				if(interval > 0){
					try{
						logger.info(">>>>>>>>>>>>>>>>>>>>>>>>休眠:{}秒", interval / 1000);
						Thread.sleep(interval);
					}catch (InterruptedException e){
						LogUtil.saveException(logger, e);
					}
					continue;
				}
				Map map = RedisUtil.getRedisHashAll(hkey);
				if(map != null && map.size() > 0){
					RedisUtil.delKey(hkey);
					List list = new ArrayList();
					int i = 0;
					for(Iterator itor = map.entrySet().iterator(); itor.hasNext();){
						Entry entry = (Entry) itor.next();
						String fieldKey = (String) entry.getKey();
						String value = (String) entry.getValue();
						String[] keys = fieldKey.split("#");
						String strResult = RedisUtil.getRedisHash(userhKey, fieldKey);
						String[] userAcc = strResult.split("#");
						LinksusRelationUserAccount userAccount = new LinksusRelationUserAccount();
						userAccount.setUserId(new Long(keys[1]));
						userAccount.setAccountId(new Long(keys[0]));
						userAccount.setInteractTime(DateUtil.getUnixDate(new Date()));
						userAccount.setInteractNum(new Long(value));
						userAccount.setInteractType(userAcc[1]);
						//设置最后互动时间
						userAccount.setInteractTime(DateUtil.getUnixDate(new Date()));
						list.add(userAccount);
						if(i % 1000 == 0 || i == map.size() - 1){
							userAccountService.updateLinksusRelationUserAccountNum(list);
							list.clear();
						}
						i++;
					}
					lastSyncTime = new Date().getTime();
				}
			}catch (NumberFormatException e){
				LogUtil.saveException(logger, e);
			}
		}
	}

	public static void main(String[] args){
		Thread thread = new Thread(new InteractCountSave());
		thread.start();
	}
}
