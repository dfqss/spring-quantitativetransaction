package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.ListingDateCalDo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ListingDateCalMapper extends BaseMapper<ListingDateCalDo> {


    /**
     * 批量插入上市日期表数据
     * @param bachMap
     * @return
     */
    int insertBatchListingDateCal(List<HashMap<String,String>> bachMap);
}
