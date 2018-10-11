package com.zyc.jobmanager.job;

import com.zyc.jobmanager.service.IUserService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: hezishan
 * Date: 2018/6/6
 * Description:
 */
@DisallowConcurrentExecution
public class MyJob implements Job, Serializable {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.datasource.url}")
    private String initMode;

    @Value("${bbd_ds.init.begin}")
    private String initBegin;

    @Value("${bbd_ds.init.end}")
    private String initEnd;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private String redisPort;

    @Autowired
    private IUserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date());
        System.out.println("id 为1的user是：" + userService.getUserById(1).getName());

        System.out.println("initMode is : " + initMode);
        System.out.println("initBegin is : " + initBegin);
        System.out.println("initEnd is :" + initEnd);
        System.out.println("redisHost is :" + redisHost);
        System.out.println("redisPort is :" + redisPort);
    }
}