package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.common.enumeration.CodeMessage;
import io.github.talelin.latticy.common.util.ResultUtil;
import io.github.talelin.latticy.scheduler.tasking.CoreIndex;
import io.github.talelin.latticy.service.CoreIndexService;
import io.github.talelin.latticy.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreIndexServiceImpl implements CoreIndexService {

    @Autowired
    private CoreIndex coreIndex ;

    @Override
    public ResultVo createOrUpdateCoreIndex() {
        try {
            coreIndex.createOrUpdateCoreIndex();
        }catch (Exception e) {
            return ResultUtil.getFailedVo(CodeMessage.CORE_INDEX_IMPORT_ERR.getCode(),
                    CodeMessage.CORE_INDEX_IMPORT_ERR.getMessage());
        }
        return ResultUtil.getSuccessVo(CodeMessage.CORE_INDEX_IMPORT_SUCCESS.getCode(),
                CodeMessage.CORE_INDEX_IMPORT_SUCCESS.getMessage());
    }
}
