package com.zyc.jobmanager.dao;

import com.zyc.jobmanager.entity.BidList;
import com.zyc.jobmanager.entity.BidListExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * BidListDAO继承基类
 */
//@Repository
@Mapper
public interface BidListDAO extends MyBatisBaseDao<BidList, Integer, BidListExample> {
    Date queryMaxBidDate();
}