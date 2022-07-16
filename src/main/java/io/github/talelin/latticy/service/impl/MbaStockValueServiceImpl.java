package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.MbaStockValueMapper;
import io.github.talelin.latticy.model.MbaStockValueDO;
import io.github.talelin.latticy.service.MbaStockValueService;
import org.springframework.stereotype.Service;

@Service
public class MbaStockValueServiceImpl extends ServiceImpl<MbaStockValueMapper, MbaStockValueDO> implements MbaStockValueService {
}
