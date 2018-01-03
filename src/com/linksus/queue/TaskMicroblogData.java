package com.linksus.queue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * ����ÿ��ָ������ץȡһ�η�˿΢���������Ӧ����
 * @author wutingfang
 *
 */
public class TaskMicroblogData extends BaseQueue{

	/** �˺����� */
	private String accountType;
	/** ���·�˿�б�������ʶλ*/
	private Long lastUserId =0L;

	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	/**
	 * ���ι�����
	 * @param taskType ��������
	 */
	protected TaskMicroblogData(String taskType) {
		super(taskType);
	}

	/**
	 * ˢ�����ݶ���
	 * 
	 */
	protected List flushTaskQueue(){
		LinksusRelationWeibouser weibouser = new LinksusRelationWeibouser();
		weibouser.setPageSize(pageSize);
		//�����û�����  1,���� 2,��Ѷ
		weibouser.setUserType(new Integer(accountType));
		//��ȡ���·�˿΢����Ƶ������
		String readSinaWeiboFrequencyDay = LoadConfig.getString("readWeiboFrequencyDay");
		Calendar calendar = Calendar.getInstance();
		//������ʱ���ȥ���õ�Ƶ����������ָ��������ǰ��ʱ��㣬��ѯʱ�Ѹ�ʱ��������ݵ�΢��������ʱ��Աȣ�
		//��������ݿ��е�ʱ�仹��˵���÷�˿��ָ���������ڼ���Ϣδ�޸Ĺ�
		calendar.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(readSinaWeiboFrequencyDay));
		Integer weiboLastSytime = Integer.parseInt(String.valueOf(calendar.getTime().getTime() / 1000));
		weibouser.setWeiboLastSytime(weiboLastSytime);
		weibouser.setLastUserId(lastUserId);
		List<LinksusRelationWeibouser> weiboUserlist = relationWeiboUserService
				.getLinksusWeibouserListByLimitDay(weibouser);
		if(weiboUserlist.size() < pageSize){
			currentPage = 1;
			setTaskFinishFlag();
			this.lastUserId = 0L;
		}else{
			currentPage++;
			this.lastUserId = weiboUserlist.get(weiboUserlist.size() - 1).getUserId();
		}
		return weiboUserlist;
	}

	/**
	 * ���ݿͻ��˷��ص��û���Ϣ�����·�˿��
	 * @param rsData �ͻ��˷��ص�����
	 */
	@SuppressWarnings("unchecked")
	protected String parseClientData(Map dataMap) throws Exception{
		LinksusRelationWeibouser weiboUser = (LinksusRelationWeibouser) dataMap.get("weiboUser");
		Integer currentTime = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		//������΢�����ͬ��ʱ��Ϊ��ǰʱ��
		weiboUser.setWeiboLastSytime(currentTime);
		QueueDataSave.addDataToQueue(weiboUser, "updatePagetimeAndMid");
		return "success";
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

}
