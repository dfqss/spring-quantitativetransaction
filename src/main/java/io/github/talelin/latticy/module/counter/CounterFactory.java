package io.github.talelin.latticy.module.counter;

import io.github.talelin.latticy.mapper.SeqSortCounterMapper;
import io.github.talelin.latticy.model.SeqSortCounterDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CounterFactory {

    // 计数器容器
    private Map<String, Counter> counterContainer = new HashMap<>() ;

    @Autowired
    private SeqSortCounterMapper seqSortCounterMapper ;

    /**
     * 初始化计数器容器
     * @param sign
     */
    private void initCounterContainer(String ...sign) {
        // 判断是否传入计数器标识
        if(null == sign || sign.length == 0) {
            // 未传入计数器标识：需要查询出所有计数器
            List<SeqSortCounterDO> seqSortCounters = this.seqSortCounterMapper.selectList(null);
            for(SeqSortCounterDO seqSortCounter : seqSortCounters) {
                setCounterContainer(seqSortCounter);
            }
        }else {
            // 传入计数器标识：根据计数器标识查询出当前计数器
            SeqSortCounterDO seqSortCounter = this.seqSortCounterMapper.selectById(sign[0]);
            setCounterContainer(seqSortCounter);
        }
    }

    /**
     * 将查询出的计数器加入数据容器中
     * @param seqSortCounter
     */
    private void setCounterContainer(SeqSortCounterDO seqSortCounter) {
        Counter counter = new Counter();
        String registerName = seqSortCounter.getRegisterName();
        counter.setCountNum(seqSortCounter.getCountNum());
        counter.setIncreaseNum(seqSortCounter.getIncreaseNum());
        counter.setEndNum(seqSortCounter.getAcquireSize() + seqSortCounter.getCountNum());
        counter.setStandby(seqSortCounter.getStandby());
        this.counterContainer.put(registerName, counter);
        // 计算出下一次的起始数据，并更新至数据库
        int nextCountNum = counter.getEndNum() + counter.getStandby();
        updateSeqSortCounter(registerName, nextCountNum);
        log.info(Thread.currentThread() + "-" + registerName + ":" + nextCountNum);
    }

    /**
     * 更新计数器表
     * @param registerName
     * @param nextCountNum
     */
    private void updateSeqSortCounter(String registerName, int nextCountNum) {
        SeqSortCounterDO SeqSortCounter = new SeqSortCounterDO();
        SeqSortCounter.setRegisterName(registerName);
        SeqSortCounter.setCountNum(nextCountNum);
        SeqSortCounter.setUpdateTime(new Date());
        this.seqSortCounterMapper.updateById(SeqSortCounter);
    }

    /**
     * 创建新的计数器
     * @param sign
     */
    private void createCounter(String sign) {
        if(isSignExist(sign)) {
            initCounterContainer(sign);
        }else {
            // 创建新的计数器到数据库
            SeqSortCounterDO seqSortCounterDO = new SeqSortCounterDO();
            seqSortCounterDO.setRegisterName(sign);
            seqSortCounterDO.setCountNum(105000);
            seqSortCounterDO.setIncreaseNum(1);
            seqSortCounterDO.setAcquireSize(4000);
            seqSortCounterDO.setStandby(1000);
            this.seqSortCounterMapper.insert(seqSortCounterDO);
            // 将计数器存入计数容器中
            Counter counter = new Counter();
            counter.setCountNum(100000);
            counter.setEndNum(104000);
            counter.setIncreaseNum(1);
            counter.setStandby(1000);
            this.counterContainer.put(sign, counter);
        }
    }

    /**
     * 判断计数器标识是否存在
     * @param sign
     * @return
     */
    private boolean isSignExist(String sign) {
        SeqSortCounterDO seqSortCounter = this.seqSortCounterMapper.selectById(sign);
        if(ObjectUtils.isEmpty(seqSortCounter)) {
            return false;
        }
        return true;
    }

    /**
     * 获取数值id
     * @param sign
     * @return
     */
    public synchronized String getNextValue(String sign) {
        // 首次初始化计数容器
        if(null == this.counterContainer || this.counterContainer.size() <= 0) {
            log.info("---------- init counter container ----------");
            initCounterContainer();
        }
        Counter counter = this.counterContainer.get(sign);
        // 容器中未获取到计数器时需要新建计数器
        if(null == counter) {
            log.info("---------- create the new counter ----------");
            createCounter(sign);
            counter = this.counterContainer.get(sign);
        }
        // 计数器内数值用完，需要从数据库中重新取值
        if(counter.isUseUp()) {
            log.info("---------- use up , rebuild the container [" + sign + "] ----------");
            initCounterContainer(sign);
        }
        return counter.getNextNum();
    }
}
