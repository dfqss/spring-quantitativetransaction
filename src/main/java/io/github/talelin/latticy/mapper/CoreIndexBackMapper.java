package io.github.talelin.latticy.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.CoreIndexBackDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreIndexBackMapper extends BaseMapper<CoreIndexBackDO> {

    void saveOrUpdateBatch(@Param("batchList") List<CoreIndexBackDO> batchList);

    List<CoreIndexBackDO> selectList(@Param("periods") int periods);

}
