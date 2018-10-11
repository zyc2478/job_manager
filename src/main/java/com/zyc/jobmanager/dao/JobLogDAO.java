package com.zyc.jobmanager.dao;

import com.zyc.jobmanager.entity.JobLog;
import com.zyc.jobmanager.entity.JobLogExample;
import org.apache.ibatis.annotations.Mapper;

/**
 * JobLogDAO继承基类
 */
@Mapper
public interface JobLogDAO extends MyBatisBaseDao<JobLog, Integer, JobLogExample> {
}