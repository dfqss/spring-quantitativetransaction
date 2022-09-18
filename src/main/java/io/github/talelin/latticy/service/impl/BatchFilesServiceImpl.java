package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.common.enumeration.CodeMessage;
import io.github.talelin.latticy.common.util.ResultUtil;
import io.github.talelin.latticy.scheduler.tasking.BatchFiles;
import io.github.talelin.latticy.service.BatchFilesService;
import io.github.talelin.latticy.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchFilesServiceImpl implements BatchFilesService {

    @Autowired
    private BatchFiles batchFiles ;

    /**
     * 新增execl文件表信息
     * @return
     */
    @Override
    public ResultVo readFile() {
        try {
            batchFiles.readFile();
        }catch (Exception e) {
            return ResultUtil.getFailedVo(CodeMessage.READ_FILE_ERR.getCode(),
                    CodeMessage.READ_FILE_ERR.getMessage());
        }
        return ResultUtil.getSuccessVo(CodeMessage.READ_FILE_SUCCESS.getCode(),
                CodeMessage.READ_FILE_SUCCESS.getMessage());
    }
}
