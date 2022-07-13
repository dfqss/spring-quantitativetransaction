package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.MbaListingDateCalDo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MbaListingDateCalMapper extends BaseMapper<MbaListingDateCalDo> {


    /**
     * 批量插入上市日期表数据
     * @param bachMap
     * @return
     */
    int insertBatchListingDateCal(List<HashMap<String,String>> bachMap);
}
