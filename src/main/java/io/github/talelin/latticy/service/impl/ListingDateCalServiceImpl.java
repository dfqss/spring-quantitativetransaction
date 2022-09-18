package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.common.enumeration.CodeMessage;
import io.github.talelin.latticy.common.util.ResultUtil;
import io.github.talelin.latticy.scheduler.tasking.ListingIndex;
import io.github.talelin.latticy.service.ListingDateCalService;
import io.github.talelin.latticy.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListingDateCalServiceImpl implements ListingDateCalService {

    @Autowired
    private ListingIndex listingIndex ;

    @Override
    public ResultVo createOrUpdateListingDateCal() {
        try {
            listingIndex.createOrUpdateListingDateCal();
        }catch (Exception e) {
            return ResultUtil.getFailedVo(CodeMessage.LISTING_DATE_CAL_ERR.getCode(),
                    CodeMessage.LISTING_DATE_CAL_ERR.getMessage());
        }
        return ResultUtil.getSuccessVo(CodeMessage.LISTING_DATE_CAL_SUCCESS.getCode(),
                CodeMessage.LISTING_DATE_CAL_SUCCESS.getMessage());
    }
}
