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
 * 关系报表记录数统计任务
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
		// 微信只有两个统计口径
		weixinmap.put("3", "sex");// 微信 性别统计口径
		weixinmap.put("4", "province");// 微信 地域统计口径

		// 微博有9个统计口径
		weibomap.put("1", "rps_verified_type");// 微博 身份统计口径 认证类型
		// weibomap.put("2", "fans_quality");//微博 粉丝质量统计口径 暂时不统计
		weibomap.put("3", "rps_gender");// 微博 性别统计口径
		weibomap.put("4", "rps_province");// 微博 地域统计口径(按照省份)
		weibomap.put("5", "TIMESTAMPDIFF(DAY,FROM_UNIXTIME(weibo_create_time),NOW())");// 微博 粉丝注册时长统计口径 (需要跟当前时间进行比对计算)
		// weibomap.put("6(组别)", "group_type");// 微博 粉丝组别统计口径
		// (需要用linksus_relation_group、linksus_relation_person_group表)
		// 单独方法
		weibomap.put("7", "rps_followers_count");// 微博 粉丝的粉丝数量统计口径
		weibomap.put("8", "rps_statuses_count");// 微博 粉丝的微博数量统计口径
		weibomap.put("9", "rps_bi_followers_count");// 微博 全部粉丝的相互关注数量统计口径
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

	// 关系报表使用次数统计更新
	@SuppressWarnings("unchecked")
	public void updateLinksusRelationUserTagCount(){
		//linksusAppaccountService.getLinksusAppaccountWXUser();
		LinksusRelationUserAccount useraccount = new LinksusRelationUserAccount();
		List<LinksusRelationUserAccount> resultList = linksusRelationUserAccountService
				.getLinksusRelationUserAccountList(useraccount);
		// 关系报表处理未处理
		for(LinksusRelationUserAccount entity : resultList){

		}
	}

	// 关系报表粉丝地域分布统计更新
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
		// map.put("accountType", type);// 微博 (新浪或腾讯)
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
				// map.put("accountType", type);//微博 (新浪或腾讯)
				linksusRelationStatisticsService.deleteLinksusRelationStatisticsByMap(map);
			}

			for(LinksusRelationUserAccount accountentity : listacoount){
				if(accountentity.getAccountType() == 1 || accountentity.getAccountType() == 2){
					for(String statistics_bore : weibomap.keySet()){
						// 微博(新浪、腾讯)粉丝统计
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
						//组别为口径的SQL						
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
						// 微信粉丝统计
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
			// 批量计算各个口径下的比例数据 statistics_scale
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

	// 关系报表粉丝地域分布统计更新(一次批量)
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

		// 微博(新浪、腾讯)粉丝统计
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
		// 微信粉丝统计
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
