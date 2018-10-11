package com.zyc.jobmanager.service;

import com.zyc.jobmanager.entity.BidList;

import java.util.Date;
import java.util.List;

public interface BidListService {

    int insertBidList(BidList bidList);

    List<BidList> getBidListByListingId(int listingId);

    List<BidList> queryBidDate(Date inputDate);

    Date queryMaxBidDate();
}
