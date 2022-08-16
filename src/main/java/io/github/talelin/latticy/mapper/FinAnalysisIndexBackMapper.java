package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.CoreIndexBackDO;
import io.github.talelin.latticy.model.FinAnalysisIndexBackDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinAnalysisIndexBackMapper extends BaseMapper<FinAnalysisIndexBackDO> {
    void saveOrUpdateBatch(@Param("batchList") List<FinAnalysisIndexBackDO> batchList);
}
