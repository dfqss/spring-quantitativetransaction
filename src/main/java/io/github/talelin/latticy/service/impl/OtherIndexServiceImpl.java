package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.common.enumeration.CodeMessage;
import io.github.talelin.latticy.common.util.ResultUtil;
import io.github.talelin.latticy.scheduler.tasking.OtherIndex;
import io.github.talelin.latticy.service.OtherIndexService;
import io.github.talelin.latticy.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherIndexServiceImpl implements OtherIndexService {

    @Autowired
    private OtherIndex otherIndex ;

    @Override
    public ResultVo createOrUpdateOtherIndex() {
        try {
            otherIndex.createOrUpdateOtherIndex();
        }catch (Exception e) {
            return ResultUtil.getFailedVo(CodeMessage.OTHER_INDEX_IMPORT_ERR.getCode(),
                    CodeMessage.OTHER_INDEX_IMPORT_ERR.getMessage());
        }
        return ResultUtil.getSuccessVo(CodeMessage.OTHER_INDEX_IMPORT_SUCCESS.getCode(),
                CodeMessage.OTHER_INDEX_IMPORT_SUCCESS.getMessage());
    }
}
