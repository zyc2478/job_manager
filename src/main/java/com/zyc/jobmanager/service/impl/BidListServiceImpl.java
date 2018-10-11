package com.zyc.jobmanager.service.impl;

import com.zyc.jobmanager.dao.BidListDAO;
import com.zyc.jobmanager.entity.BidList;
import com.zyc.jobmanager.entity.BidListExample;
import com.zyc.jobmanager.service.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//@Service("bidListService")
@Service
public class BidListServiceImpl implements BidListService {

    //@Resource
    @Autowired
    private BidListDAO bidListDao;

    @Override
    public int insertBidList(BidList bidList) { return this.bidListDao.insert(bidList); }

    BidListExample bidListExample = new BidListExample();

    @Override
    public List<BidList> getBidListByListingId(int listingId) {

        bidListExample.createCriteria().andListingIdEqualTo(new Integer(listingId));
        return this.bidListDao.selectByExample(bidListExample);
    }

    @Override
    public List<BidList> queryBidDate(Date bidDate) {
        bidListExample.createCriteria().andBidDateEqualTo(bidDate);
        return this.bidListDao.selectByExample(bidListExample);
    }

    @Override
    public Date queryMaxBidDate() {
        return this.bidListDao.queryMaxBidDate();
    }

}