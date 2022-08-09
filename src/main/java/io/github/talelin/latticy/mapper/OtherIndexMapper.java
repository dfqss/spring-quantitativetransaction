package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.bo.EntityBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtherIndexMapper {

    void saveOrUpdateBatch(@Param("batchList") List<Object> batchList, @Param("entity") EntityBo entity);
}
