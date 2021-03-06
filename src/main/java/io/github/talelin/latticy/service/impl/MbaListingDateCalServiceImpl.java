package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.MbaListingDateCalMapper;
import io.github.talelin.latticy.model.MbaListingDateCalDo;
import io.github.talelin.latticy.service.MbaListingDateCalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class MbaListingDateCalServiceImpl extends ServiceImpl<MbaListingDateCalMapper, MbaListingDateCalDo> implements MbaListingDateCalService {

    @Autowired
    private MbaListingDateCalMapper mbaListingDateCalMapper;

    /**
     * 批量插入上市日期表数据
     * @param bachMap
     * @return
     */
    @Override
    @Transactional
    public int insertBatchListingDateCal(List<HashMap<String, String>> bachMap) {
        mbaListingDateCalMapper.insertBatchListingDateCal(bachMap);
        return 0;
    }
}
