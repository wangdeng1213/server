package com.linksus.queue;

import java.util.List;
import java.util.Map;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.ResponseAndRecordDTO;
import com.linksus.service.LinksusAttentionWeiboService;
import com.linksus.service.LinksusWeiboPoolService;
import com.linksus.service.LinksusWeiboService;

public class TaskWeiboDynaData extends BaseQueue{

	private String dataType;// ΢��/��ע�û�΢��
	private String accountType;// �˺�����

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	protected TaskWeiboDynaData(String taskType) {
		super(taskType);
	}

	LinksusWeiboService linksusWeiboService = (LinksusWeiboService) ContextUtil.getBean("linksusWeiboService");
	LinksusAttentionWeiboService linksusAttentionWeiboService = (LinksusAttentionWeiboService) ContextUtil
			.getBean("linksusAttentionWeiboService");
	LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");

	@Override
	public List flushTaskQueue(){
		// �ӷ������˲��÷�ҳȡ����
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// ͨ����ҳȡ��ֱ���б�
		LinksusWeibo weibo = new LinksusWeibo();
		weibo.setStartCount(currenCount);
		weibo.setPageSize(pageSize);
		weibo.setAccountType(new Integer(accountType));// 1,���� 2,��Ѷ
		List<LinksusWeibo> weiboList = null;
		if("1".equals(dataType)){//��¼�˻�������������΢����Ϣ
			weiboList = linksusWeiboService.getLinksusWeiboHasSend(weibo);
		}else if("2".equals(dataType)){//�ʺŹ�ע΢���б���Ϣ
			weiboList = linksusAttentionWeiboService.getLinksusAttentionWeiboHasSend(weibo);
		}else if("3".equals(dataType)){//��������ϵ����������������΢����Ϣ��
			weiboList = linksusWeiboPoolService.getLinksusWeiboPoolSend(weibo);
		}else{
			LogUtil.saveException(logger, new Exception("dataType��������"));
		}
		if(weiboList == null || weiboList.size() < pageSize){// ����ѭ����� �´����¿�ʼ
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}
		return weiboList;
	}

	@Override
	protected String parseClientData(Map rsDataMap){
		List<ResponseAndRecordDTO> linksusWeibos = (List<ResponseAndRecordDTO>) rsDataMap.get("weiboList");
		if(linksusWeibos != null && linksusWeibos.size() > 0){
			for(ResponseAndRecordDTO dto : linksusWeibos){
				if("1".equals(dataType)){
					//΢������
					LinksusWeibo linksusWeibo = new LinksusWeibo();
					linksusWeibo.setCommentCount(dto.getComments());
					linksusWeibo.setRepostCount(dto.getReposts());
					linksusWeibo.setMid(dto.getId());
					linksusWeibo.setAccountType(Integer.parseInt(accountType));
					//linksusWeiboService.updateSendWeiboCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusWeibo, Constants.OPER_TYPE_UPDATE);
				}else if("2".equals(dataType)){
					//��ע�û�
					LinksusAttentionWeibo linksusAttentionWeibo = new LinksusAttentionWeibo();
					linksusAttentionWeibo.setCommentCount(dto.getComments());
					linksusAttentionWeibo.setRepostCount(dto.getReposts());
					linksusAttentionWeibo.setMid(dto.getId());
					linksusAttentionWeibo.setAccountType(Integer.parseInt(accountType));
					//linksusAttentionWeiboService.updateSendWeiboCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusAttentionWeibo, Constants.OPER_TYPE_UPDATE);
				}else if("3".equals(dataType)){
					//΢����
					LinksusWeiboPool linksusWeiboPool = new LinksusWeiboPool();
					linksusWeiboPool.setCommentCount(dto.getComments());
					linksusWeiboPool.setRepostCount(dto.getReposts());
					linksusWeiboPool.setMid(dto.getId());
					linksusWeiboPool.setAccountType(Integer.parseInt(accountType));
					//linksusWeiboPoolService.updateLinksusWeiboPoolCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusWeiboPool, Constants.OPER_TYPE_UPDATE);
				}
			}
		}
		return "success";
	}
}
