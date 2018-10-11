package com.zyc.jobmanager.service.impl;

import com.zyc.jobmanager.dao.JobLogDAO;
import com.zyc.jobmanager.entity.JobLog;
import com.zyc.jobmanager.service.JobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("jobLogService")
public class JobLogServiceImpl implements JobLogService {


    @Resource
    private JobLogDAO jobLogDAO;

    @Override
    public int insertJobLog(JobLog jobLog) {
        return this.jobLogDAO.insert(jobLog);
    }
}
