package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.MbaListingDateCalDo;

import java.util.HashMap;
import java.util.List;

public interface MbaListingDateCalService extends IService<MbaListingDateCalDo> {


    /**
     * 批量插入上市日期表数据
     */
    int insertBatchListingDateCal(List<HashMap<String,String>> bachMap);
}
