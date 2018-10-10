package com.zyc.jobmanager.controller;

import com.zyc.jobmanager.constant.ServerErrorEnum;
import com.zyc.jobmanager.exception.ServerException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 定时任务 管理
 * <p/>
 * User: lishaohua
 * Date: 2017/11/14  13:22
 */
@Controller
@Api("定时任务管理")//描述类的作用(Swagger注解)
public class JobController {
    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    /*@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;*/

    @Autowired
    private Scheduler scheduler;


    /**
     * 添加定时任务
     *
     * @param request
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @return
     */
    @ApiOperation("添加定时任务")//描述方法的作用(Swagger注解)
    @RequestMapping(value = "/addJob", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> addJob(HttpServletRequest request,
                                      @RequestParam(value = "jobClassName") String jobClassName,
                                      @RequestParam(value = "jobGroupName") String jobGroupName,
                                      @RequestParam(value = "cronExpression") String cronExpression) {
        Map<String, String> returnData = new HashMap<String, String>();
        try {
            /**
             * 构建JobDetail(表示一个具体的可执行的调度程序,Job是这个可执行程调度程序所要执行的内容)
             */
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())//工作项1：Job类
                    .withIdentity(jobClassName, jobGroupName)//工作项2：job名以及所属组
                    .build();//构建

            /**
             * 构建触发器Trigger(调度参数的配置，代表何时触发该任务)
             */
            //通过cron表达式构建CronScheduleBuilder
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            //构建CronTrigger触发器
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobClassName, jobGroupName) //工作项1：job名以及所属组
                    .withSchedule(scheduleBuilder) //工作项2：指定调度参数
                    .build();//构建

            /**
             *构建调度容器(当Trigger与JobDetail组合，就可以被Scheduler容器调度了)
             * 一个调度容器中可以注册多个JobDetail和Trigger。
             */
            //获得调度容器
            //Scheduler scheduler = getCurrentScheduler();
            //注册调度任务
            scheduler.scheduleJob(jobDetail, trigger);
            //启动任务
            scheduler.start();

            returnData.put("msg", "添加调度任务成功");
        } catch (SchedulerException e) {
            logger.error("构建调度任务异常", e);
            returnData.put("msg", "添加调度任务异常：" + e.getMessage());
        } catch (ServerException e) {
            logger.error("内部异常", e);
            returnData.put("msg", "添加调度任务异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("添加调度任务异常", e);
            returnData.put("msg", "添加调度任务异常：" + e.getMessage());
        }

        return returnData;
    }

    @ApiOperation("暂停定时任务")//描述方法的作用(Swagger注解)
    @RequestMapping(value = "/pauseJob", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> pauseJob(HttpServletRequest request,
                                        @RequestParam(value = "jobClassName") String jobClassName,
                                        @RequestParam(value = "jobGroupName") String jobGroupName) {
        Map<String, String> returnData = new HashMap<String, String>();
        try {
            //获得调度容器
            //Scheduler scheduler = getCurrentScheduler();
            //JobKey定义了job的名称和组别
            JobKey jobKey = JobKey.jobKey(jobClassName, jobGroupName);
            //暂停任务
            scheduler.pauseJob(jobKey);

            returnData.put("msg", "暂停调度任务成功");
        } catch (SchedulerException e) {
            logger.error("暂停调度任务异常", e);
            returnData.put("msg", "暂停调度任务异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("暂停调度任务异常", e);
            returnData.put("msg", "暂停调度任务异常：" + e.getMessage());
        }

        return returnData;
    }

    @ApiOperation("继续定时任务")//描述方法的作用(Swagger注解)
    @RequestMapping(value = "/resumeJob", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> resumeJob(HttpServletRequest request,
                                         @RequestParam(value = "jobClassName") String jobClassName,
                                         @RequestParam(value = "jobGroupName") String jobGroupName) {
        Map<String, String> returnData = new HashMap<String, String>();
        try {
            //获得调度容器
            //Scheduler scheduler = getCurrentScheduler();
            //JobKey定义了job的名称和组别
            JobKey jobKey = JobKey.jobKey(jobClassName, jobGroupName);
            //继续任务
            scheduler.resumeJob(jobKey);

            returnData.put("msg", "继续调度任务成功");
        } catch (SchedulerException e) {
            logger.error("继续调度任务异常", e);
            returnData.put("msg", "继续调度任务异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("继续调度任务异常", e);
            returnData.put("msg", "继续调度任务异常：" + e.getMessage());
        }

        return returnData;
    }

    /**
     * 更新定时任务：
     * --传入的triggerKey有与之匹配的
     * --旧触发器的触发时间没有完成
     *
     * @param request
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @return
     */
    @ApiOperation("更新定时任务")//描述方法的作用(Swagger注解)
    @RequestMapping(value = "/rescheduleJob", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> rescheduleJob(HttpServletRequest request,
                                             @RequestParam(value = "jobClassName") String jobClassName,
                                             @RequestParam(value = "jobGroupName") String jobGroupName,
                                             @RequestParam(value = "cronExpression") String cronExpression) {
        Map<String, String> returnData = new HashMap<String, String>();
        try {
            //获得调度容器
            //Scheduler scheduler = getCurrentScheduler();

            //构建旧的TriggerKey
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            //通过cron表达式构建CronScheduleBuilder
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            //从调度容器中获取旧的CronTrigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //更新CronTrigger
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey) //工作项1：job名以及所属组
                    .withSchedule(scheduleBuilder) //工作项2：指定调度参数
                    .build();//构建

            //更新调度任务
            scheduler.rescheduleJob(triggerKey, trigger);

            returnData.put("msg", "更新调度任务成功");
        } catch (SchedulerException e) {
            logger.error("更新调度任务异常", e);
            returnData.put("msg", "更新调度任务异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("更新调度任务异常", e);
            returnData.put("msg", "更新调度任务异常：" + e.getMessage());
        }

        return returnData;
    }


    @ApiOperation("删除定时任务")//描述方法的作用(Swagger注解)
    @RequestMapping(value = "/removeJob", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> removeJob(HttpServletRequest request,
                                         @RequestParam(value = "jobClassName") String jobClassName,
                                         @RequestParam(value = "jobGroupName") String jobGroupName) {
        Map<String, String> returnData = new HashMap<String, String>();
        try {
            //获得调度容器
            //Scheduler scheduler = getCurrentScheduler();
            //TriggerKey定义了trigger的名称和组别
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);

            //暂停触发器
            scheduler.resumeTrigger(triggerKey);
            //暂停触发器
            scheduler.unscheduleJob(triggerKey);
            //移除任务
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));

            returnData.put("msg", "删除调度任务成功");
        } catch (SchedulerException e) {
            logger.error("删除调度任务异常", e);
            returnData.put("msg", "删除调度任务异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("删除调度任务异常", e);
            returnData.put("msg", "删除调度任务异常：" + e.getMessage());
        }

        return returnData;
    }

    /**
     * 获得调度容器Scheduler
     *
     * @return
     * @throws SchedulerException
     */
    /*private Scheduler getCurrentScheduler() throws SchedulerException {
        // 实例化Quartz默认的调度器工厂SchedulerFactory
        //SchedulerFactory sf = new StdSchedulerFactory();
        // 获得调度容器
        //Scheduler sched = sf.getScheduler();
        //return sched;
        //Scheduler sched = schedulerFactoryBean.getScheduler();
        //return sched;
    }*/


    /**
     * 获得指定的类实例
     *
     * @param classname
     * @return
     * @throws ServerException
     */
    private Job getClass(String classname) throws ServerException {
        Job baseJob = null;
        try {
            //加载参数指定的类
            Class<?> classTmp = Class.forName(classname);
            //实例化
            baseJob = (Job) classTmp.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("找不到指定的类", e);
            throw new ServerException(ServerErrorEnum.INTERNAL_ERROR);
        } catch (InstantiationException e) {
            logger.error("实例化类失败", e);
            throw new ServerException(ServerErrorEnum.INTERNAL_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("实例化类失败", e);
            throw new ServerException(ServerErrorEnum.INTERNAL_ERROR);
        }

        return baseJob;
    }
}