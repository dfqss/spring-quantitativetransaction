package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.RangeRiseCommonDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RangeRiseCommonMapper extends BaseMapper<RangeRiseCommonDO> {
    void saveOrUpdateBatch(@Param("batchList") List<RangeRiseCommonDO> batchList);
}
