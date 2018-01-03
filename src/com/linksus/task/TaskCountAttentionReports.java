package com.linksus.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.entity.LinksusRelationStatistics;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusRelationStatisticsService;
import com.linksus.service.LinksusRelationUserAccountService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRelationWxuserService;

/**
 * ��ϵ�����¼��ͳ������
 */
public class TaskCountAttentionReports extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskCountAttentionReports.class);

	LinksusRelationUserAccountService linksusRelationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");

	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");
	LinksusRelationStatisticsService linksusRelationStatisticsService = (LinksusRelationStatisticsService) ContextUtil
			.getBean("linksusRelationStatisticsService");

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	static TreeMap<String, String> weixinmap = new TreeMap();
	static TreeMap<String, String> weibomap = new TreeMap();

	static{
		// ΢��ֻ������ͳ�ƿھ�
		weixinmap.put("3", "sex");// ΢�� �Ա�ͳ�ƿھ�
		weixinmap.put("4", "province");// ΢�� ����ͳ�ƿھ�

		// ΢����9��ͳ�ƿھ�
		weibomap.put("1", "rps_verified_type");// ΢�� ���ͳ�ƿھ� ��֤����
		// weibomap.put("2", "fans_quality");//΢�� ��˿����ͳ�ƿھ� ��ʱ��ͳ��
		weibomap.put("3", "rps_gender");// ΢�� �Ա�ͳ�ƿھ�
		weibomap.put("4", "rps_province");// ΢�� ����ͳ�ƿھ�(����ʡ��)
		weibomap.put("5", "TIMESTAMPDIFF(DAY,FROM_UNIXTIME(weibo_create_time),NOW())");// ΢�� ��˿ע��ʱ��ͳ�ƿھ� (��Ҫ����ǰʱ����бȶԼ���)
		// weibomap.put("6(���)", "group_type");// ΢�� ��˿���ͳ�ƿھ�
		// (��Ҫ��linksus_relation_group��linksus_relation_person_group��)
		// ��������
		weibomap.put("7", "rps_followers_count");// ΢�� ��˿�ķ�˿����ͳ�ƿھ�
		weibomap.put("8", "rps_statuses_count");// ΢�� ��˿��΢������ͳ�ƿھ�
		weibomap.put("9", "rps_bi_followers_count");// ΢�� ȫ����˿���໥��ע����ͳ�ƿھ�
	}

	@Override
	public void cal(){

		fansRelationStatistics("1");
	}

	public void fansRelationStatistics(String type){

		insertALLLinksusRelationStatistics("3");
		// for (String key : weibomap.keySet()) {
		// insertALLLinksusRelationStatistics(key, "1");
		// }
		// for (String key : weibomap.keySet()) {
		// insertALLLinksusRelationStatistics(key, "2");
		// }
		// for (String key : weixinmap.keySet()) {
		// insertALLLinksusRelationStatistics(key, "3");
		// }
	}

	// ��ϵ����ʹ�ô���ͳ�Ƹ���
	@SuppressWarnings("unchecked")
	public void updateLinksusRelationUserTagCount(){
		//linksusAppaccountService.getLinksusAppaccountWXUser();
		LinksusRelationUserAccount useraccount = new LinksusRelationUserAccount();
		List<LinksusRelationUserAccount> resultList = linksusRelationUserAccountService
				.getLinksusRelationUserAccountList(useraccount);
		// ��ϵ������δ����
		for(LinksusRelationUserAccount entity : resultList){

		}
	}

	// ��ϵ�����˿����ֲ�ͳ�Ƹ���
	public void insertALLLinksusRelationStatistics(String type){
		// List<LinksusAppaccount> list = linksusAppaccountService
		// .getALLLinksusAppaccountList(new HashMap());
		// Map map = new HashMap();
		// for (LinksusAppaccount entity : list) {
		// map.put("accountId", entity.getId());
		// linksusRelationStatisticsService
		// .deleteLinksusRelationStatisticsByMap(map);
		// }
		LinksusRelationStatistics pmentity = new LinksusRelationStatistics();
		Map map = new HashMap();
		// map.put("accountType", type);// ΢�� (���˻���Ѷ)
		String batchsql = "";
		Long pid = null;
		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		List<LinksusRelationUserAccount> listacoount = linksusRelationUserAccountService
				.getALLRelationUserAccountMap(map);
		List<LinksusRelationStatistics> rsList = new ArrayList();
		try{
			for(LinksusRelationUserAccount accountentity : listacoount){
				map.put("accountId", accountentity.getAccountId());
				// map.put("statistics_bore", statistics_bore);
				// map.put("accountType", type);//΢�� (���˻���Ѷ)
				linksusRelationStatisticsService.deleteLinksusRelationStatisticsByMap(map);
			}

			for(LinksusRelationUserAccount accountentity : listacoount){
				if(accountentity.getAccountType() == 1 || accountentity.getAccountType() == 2){
					for(String statistics_bore : weibomap.keySet()){
						// ΢��(���ˡ���Ѷ)��˿ͳ��
						batchsql = " SELECT " + "  T.institution_id," + "  T.account_id," + "  T.account_type,"
								+ "  1 as flag_relation,"
								+ statistics_bore
								+ "  as statistics_bore,"
								+ weibomap.get(statistics_bore)
								+ "  as statistics_type,"
								+ "  COUNT(*) statistics_count "
								+ " FROM linksus_relation_user_account T,linksus_relation_weibouser T1"
								+ " WHERE  T.user_id = T1.user_id "
								// + " AND T.account_type in (1,2) "
								// + " AND T.flag_relation = 1"
								+ " AND T.account_id = " + accountentity.getAccountId()
								+ " GROUP BY T.institution_id,T.account_id, T.account_type,"
								+ weibomap.get(statistics_bore);
						//���Ϊ�ھ���SQL						
						//						batchsql = " SELECT "
						//							+ "  T.institution_id,"
						//							+ "  T.account_id,"
						//							+ "  T.account_type,"
						//							+ "  1 as flag_relation,"
						//							+ statistics_bore
						//							+ "  as statistics_bore,"
						//							+ weibomap.get(statistics_bore)
						//							+ "  as statistics_type,"
						//							+ "  COUNT(*) statistics_count "
						//							+ " FROM linksus_relation_user_account T,linksus_relation_weibouser T1,linksus_relation_person_group G1,linksus_relation_group G2 "
						//							+ " WHERE  T.user_id = T1.user_id "
						//							// + " AND T.account_type in (1,2) "
						//							// + " AND T.flag_relation = 1"
						//							+ " AND T.account_id = "
						//							+ accountentity.getAccountId()
						//							+ " GROUP BY T.institution_id,T.account_id, T.account_type,T1.person_id"
						//							+ weibomap.get(statistics_bore);

						pmentity.setBatchsql(batchsql);
						List<LinksusRelationStatistics> liststatistics = linksusRelationStatisticsService
								.getLinksusRelationStatisticsListByBean(pmentity);
						rsList.addAll(liststatistics);
					}

				}else{
					for(String statistics_bore : weixinmap.keySet()){
						// ΢�ŷ�˿ͳ��
						batchsql = " SELECT " + "  T.institution_id," + "  T.account_id," + "  T.account_type,"
								+ "  1 as flag_relation,"
								+ statistics_bore
								+ "  as statistics_bore,"
								+ weixinmap.get(statistics_bore)
								+ "  as statistics_type,"
								+ "  COUNT(*) statistics_count "
								+ " FROM linksus_relation_user_account T,linksus_relation_wxuser T1"
								+ " WHERE  T.user_id = T1.user_id "
								// + " AND T.account_type = 3"
								// + " AND T.flag_relation = 3"
								+ " AND T.account_id = " + accountentity.getAccountId()
								+ " GROUP BY T.institution_id,T.account_id, T.account_type,"
								+ weixinmap.get(statistics_bore);

						pmentity.setBatchsql(batchsql);
						List<LinksusRelationStatistics> liststatistics = linksusRelationStatisticsService
								.getLinksusRelationStatisticsListByBean(pmentity);
						rsList.addAll(liststatistics);
					}
				}

			}

			for(LinksusRelationStatistics entity : rsList){
				pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_statistics", "pid");
				entity.setPid(pid);
				entity.setStatisticsScale(0);
				entity.setStartTime(0);
				entity.setEndTime(0);
				entity.setLastUpdtTime(last_updt_time);
				linksusRelationStatisticsService.insertLinksusRelationStatistics(entity);
			}
			// ������������ھ��µı������� statistics_scale
			batchsql = " UPDATE linksus_relation_statistics t"
					+ " SET t.statistics_scale =  CAST((t.statistics_count/(SELECT statistics_count FROM ("
					+ " SELECT t2.account_id,t2.account_type,t2.statistics_bore,SUM(t2.statistics_count) as statistics_count"
					+ " from linksus_relation_statistics t2"
					+ " GROUP BY t2.account_id,t2.account_type,t2.statistics_bore" + " ) t1"
					+ " where t.account_id = t1.account_id " + " and t.account_type = t1.account_type"
					+ " and t.statistics_bore = t1.statistics_bore" + " ))*100 AS DECIMAL(8,4)) ";

			pmentity.setBatchsql(batchsql);
			linksusRelationStatisticsService.insertALLLinksusRelationStatistics(pmentity);

		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// ��ϵ�����˿����ֲ�ͳ�Ƹ���(һ������)
	public void insertALLLinksusRelationStatisticsbatch(){
		// List<LinksusAppaccount> list = linksusAppaccountService
		// .getALLLinksusAppaccountList(new HashMap());
		// Map map = new HashMap();
		// for (LinksusAppaccount entity : list) {
		// map.put("accountId", entity.getId());
		// linksusRelationStatisticsService
		// .deleteLinksusRelationStatisticsByMap(map);
		// }
		Map map = new HashMap();
		String batchsql = "";
		List<LinksusRelationUserAccount> listacoount = linksusRelationUserAccountService
				.getALLRelationUserAccountMap(new HashMap());
		for(LinksusRelationUserAccount entity : listacoount){
			map.put("accountId", entity.getAccountId());
			linksusRelationStatisticsService.deleteLinksusRelationStatisticsByMap(map);
		}
		LinksusRelationStatistics entity = new LinksusRelationStatistics();

		// ΢��(���ˡ���Ѷ)��˿ͳ��
		batchsql = " INSERT INTO linksus_relation_statistics (institution_id,account_id,account_type,flag_relation,statistics_bore,statistics_type,statistics_count) "
				+ " SELECT  T.institution_id,"
				+ "  T.account_id,"
				+ "  T.account_type,"
				+ "  1 as flag_relation,"
				+ "  4 as statistics_bore,"
				+ "  rps_province statistics_type,"
				+ "  COUNT(*) statistics_count "
				+ " FROM linksus_relation_user_account T,linksus_relation_weibouser T1"
				+ " WHERE  T.user_id = T1.user_id AND T.flag_relation = 1"
				+ " AND T.account_type in (1,2) "
				+ " GROUP BY T.institution_id,T.account_id, T.account_type,T1.rps_province";
		entity.setBatchsql(batchsql);
		linksusRelationStatisticsService.insertALLLinksusRelationStatistics(entity);
		// ΢�ŷ�˿ͳ��
		batchsql = " INSERT INTO linksus_relation_statistics (institution_id,account_id,account_type,flag_relation,statistics_bore,statistics_type,statistics_count) "
				+ " SELECT  T.institution_id,"
				+ "  T.account_id,"
				+ "  T.account_type,"
				+ "  1 as flag_relation,"
				+ "  4 as statistics_bore,"
				+ "  T1.province statistics_type,"
				+ "  COUNT(*) statistics_count "
				+ " FROM linksus_relation_user_account T,linksus_relation_wxuser T1"
				+ " WHERE  T.user_id = T1.user_id AND T.flag_relation = 3"
				+ " AND T.account_type = 3"
				+ " GROUP BY T.institution_id,T.account_id, T.account_type,T1.province";

		entity.setBatchsql(batchsql);
		linksusRelationStatisticsService.insertALLLinksusRelationStatistics(entity);
	}

	public static void main(String[] args){
		TaskCountAttentionReports ta = new TaskCountAttentionReports();
		// ta.insertALLLinksusRelationStatistics("4");
		ta.fansRelationStatistics("");
		// ta.insertALLLinksusRelationStatisticsbatch();
	}
}
