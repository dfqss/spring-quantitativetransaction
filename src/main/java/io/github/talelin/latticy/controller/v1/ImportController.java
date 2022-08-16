package io.github.talelin.latticy.controller.v1;

import io.github.talelin.latticy.service.BatchFilesService;
import io.github.talelin.latticy.service.CoreIndexService;
import io.github.talelin.latticy.service.OtherIndexService;
import io.github.talelin.latticy.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quaTraV1/importData")
@Validated
@Slf4j
public class ImportController {

    @Autowired
    private BatchFilesService batchFilesService ;

    @Autowired
    private CoreIndexService coreIndexService ;

    @Autowired
    private OtherIndexService otherIndexService ;

    /**
     * 批量文件入库
     * @return
     */
    @PostMapping("readCoreIndexExcel")
    public ResultVo readCoreIndexExcel() {
        log.info("start service readCoreIndexExcel from api");
        return batchFilesService.readFile();
    }

    /**
     * 读取核心指标数据
     * @return
     */
    @PostMapping("importCoreIndexData")
    public ResultVo importCoreIndexData() {
        log.info("start service importCoreIndexData from api");
        return coreIndexService.createOrUpdateCoreIndex();
    }

    /**
     * 读取其他指标数据
     * @return
     */
    @PostMapping("importOtherIndexData")
    public ResultVo importOtherIndexData() {
        log.info("start service importOtherIndexData from api");
        return otherIndexService.createOrUpdateOtherIndex();
    }
}
