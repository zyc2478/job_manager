package com.zyc.jobmanager.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.jobmanager.api.PPDApiService;
import com.zyc.jobmanager.config.MyProps;
import com.zyc.jobmanager.entity.BidList;
import com.zyc.jobmanager.entity.JobLog;
import com.zyc.jobmanager.service.BidListService;
import com.zyc.jobmanager.service.JobLogService;
import com.zyc.jobmanager.util.*;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

//@Component
/*@EnableScheduling
@Service*/
//public class BidListJob {
public class BidListJob implements Job, Serializable{

    // 该类必须为public修饰
    // 该类必须含有空参数的构造器

    MyProps props = ConfigUtil.getProps();

    RedisUtil redisUtil = new RedisUtil();

    TokenUtil tokenUtil = new TokenUtil();

    private static final long serialVersionUID = 1L;

    @Autowired
    private BidListService bidListService ;

    @Autowired
    private JobLogService jobLogService ;

    private static Logger logger = Logger.getLogger("BidListJob.class");

    private int initMode = props.getInit_mode();

    private String initBegin = props.getInit_begin();

    private String initEnd = props.getInit_end();

    private String token = tokenUtil.getToken();



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 传入的参数
        JobDataMap params = context.getJobDetail().getJobDataMap();

        //业务逻辑
        System.out.println("The Job will starting!");

        System.out.println("initMode: " + initMode);
        System.out.println("initBegin:" + initBegin);
/*        System.out.println("redisHost in BidListJob: " + redisHost);
        System.out.println("redisPort in BidListJob: " + redisPort);*/

        //System.out.println("The result is: " + bidListService.getBidListByListingId(62068730));
        if( initMode==1){
            try {
                fetchSomeDays(initBegin,initEnd);
                //bidListService.getBidListByListingId(62068730);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if( initMode==0 ){
            try {
                fetchDaysUntilNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("initMode in configuration must equals 0 or 1 !");
        }
    }

    public void executeJob() throws Exception {
        execute(null);
        //executeInternal(null);
        //System.out.println("initMode=" + initMode);
        //fetchDaysUntilNow();
        //System.out.println("The bidList json is : " + bidListService.getBidListByListingId(62068730));
    }

    public void init() {
        try {
            tokenUtil.init();
            token = tokenUtil.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fetchBidList(String startDate,String endDate) throws Exception {

        //RedisUtil redisUtil = new RedisUtil(redisHost,redisPort);
        Jedis jedis = redisUtil.getJedis();
        init();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_detail = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_time = null;
        Date end_time = null;
        Date bidDate = sdf.parse(endDate);

        int pageIndex = 1, pageSize = 50;
        int bidCount;
        start_time = new Date();

        do{
            //System.out.println("in while");
            bidCount = 0;
            JSONArray bidListArray = PPDApiService.bidList(token,startDate, endDate,pageIndex,pageSize);
            for(Object bidListObj:bidListArray){

                JSONObject bidListJO = JSONObject.parseObject(bidListObj.toString());
                //System.out.println("bidListJO: " + bidListJO);
                JSONObject transBidListJO = JSONUtil.transFirstLowerObj(bidListJO);
                //System.out.println("transBidListJO: " + transBidListJO);
                System.out.println(bidListJO);

                if(transBidListJO.getIntValue("listingId")!=0 && transBidListJO.getString("title")!=null){

                    BidList bl = JSONObject.toJavaObject(transBidListJO,BidList.class);
                    bl.setBidDate(bidDate);
                    //System.out.println("bl: " + bl);

                    System.out.println("insert " + bidListService.insertBidList(bl) + " record(s) in bid_list");

                }else{
                    logger.warning( bidDate + " 没有投标！");
                }
                bidCount++;
                System.out.println("bidCount:" + bidCount);
            }
            pageIndex++;
            System.out.println("pageIndex: " + pageIndex);

        }while(bidCount == 50);

        end_time = new Date();
        System.out.println("end_time: "+ end_time);
        jedis.set("job_bid_list",endDate);

        JobLog jobLog = new JobLog();
        jobLog.setJob_name("bid_list" + "_" + endDate);
        jobLog.setJob_type("bid_list");
        jobLog.setRun_result("Successful");
        jobLog.setStart_time(start_time);
        jobLog.setEnd_time(end_time);

        System.out.println("insert " + jobLogService.insertJobLog(jobLog) + " record(s) in job_log");
    }

    //@Test
    public void fetchDaysUntilNow() throws Exception {
        //RedisUtil redisUtil = new RedisUtil(redisHost,redisPort);
        Jedis jedis = redisUtil.getJedis();

        String saveDate = jedis.get("job_bid_list");
        System.out.println("get saveDate: " + saveDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 1000 * 24 * 3600);
        String lastDateStr = sdf.format(yesterday);
        //lastDateStr = "2018-8-27";
        Date lastDate = sdf.parse(lastDateStr);
        Date maxDate = bidListService.queryMaxBidDate();
        System.out.println("maxDate：" + maxDate);
        Date beginDate = DateUtil.calcBeginDate(maxDate,-1);
        System.out.println(sdf.format(beginDate));
        int diffDays = DateUtil.differentDaysByMillisecond(beginDate,new Date());
        System.out.println("diffDays: " + diffDays);

        if(diffDays==0){
            System.out.println("数据库已有日期"+maxDate+"的记录");
        }

        for(int i = 0; i < diffDays; i++){
            String runDateStr = sdf.format(beginDate);
            System.out.println("runDateStr:" + runDateStr);
            Date runDate = sdf.parse(runDateStr);
            System.out.println(runDate);
            List<BidList> bidLists = bidListService.queryBidDate(sdf.parse(runDateStr));
            System.out.println(bidLists);
            if(bidLists.size()!=0){
                System.out.println("数据库已有该日期的记录，共" + bidLists.size() + "条");
            }else if(saveDate.equals(lastDate)){
                logger.warning("该日期已经执行过bid_list任务");
            }else{
                System.out.println("start running, date is : " + runDateStr);
                this.fetchBidList(runDateStr,runDateStr);
            }

            beginDate = DateUtil.calcBeginDate(beginDate,-1);
        }
        List<BidList> bidLists = bidListService.queryBidDate(lastDate);
    }

    //@Test
    public void testJobLogInsert() throws Exception {
        JobLog jobLog = new JobLog();
        jobLog.setJob_name("bid_list" + "_" + "2018-08-07");
        jobLog.setJob_type("bid_list");
        Date testDate = new Date();
        jobLog.setRun_result("Successful");
        jobLog.setStart_time(testDate);
        jobLog.setEnd_time(testDate);

        System.out.println("insert " + jobLogService.insertJobLog(jobLog) + " record(s) in job_log");

    }
    //@Test
    public void fetchSomeDays(String beginDateInput,String endDateInput) throws Exception {

        String start = beginDateInput;
        String end = endDateInput;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);
        long diff = (endDate.getTime() - startDate.getTime())/1000/3600/24;
        System.out.println("diff:" + diff);

        for(int i=0;i<diff+1;i++){
            Date runDate = new Date(startDate.getTime()+ 1000 * 24 * 3600 * i);
            String dateStr = sdf.format(runDate);
            System.out.println("投标日期：" + dateStr);
            this.fetchBidList(dateStr,dateStr);
        }
    }

    //@Test
    public void initAll() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beginDateStr = "2017-07-27";
        Date beginDate = sdf.parse(beginDateStr);

        int diffDays = DateUtil.differentDaysByMillisecond(beginDate,new Date());
        System.out.println("diffDays: " + diffDays);

        for(int i = 0; i < diffDays; i++){
            this.fetchBidList(sdf.format(beginDate),sdf.format(beginDate));
            beginDate = DateUtil.calcBeginDate(beginDate,-1);
            System.out.println(i);
        }
    }

}
