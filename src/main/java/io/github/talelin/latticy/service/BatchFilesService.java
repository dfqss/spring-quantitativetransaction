package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.BatchFilesDO;
import io.github.talelin.latticy.vo.ResultVo;

import java.util.List;

public interface BatchFilesService {

    /**
     * 新增execl文件表信息
     * @return
     */
    ResultVo readFile() ;
}
