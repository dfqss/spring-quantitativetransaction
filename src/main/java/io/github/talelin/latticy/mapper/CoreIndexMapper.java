package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.CoreIndexDO;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreIndexMapper extends BaseMapper<CoreIndexDO> {

    void backupCoreIndex();

    void updateCalDate(String calDate, String reportDate);

    void updateShowTimes();

    void updatePeriods(Integer filePeriods);

}
