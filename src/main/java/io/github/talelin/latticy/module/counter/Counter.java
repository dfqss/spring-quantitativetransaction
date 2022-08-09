package io.github.talelin.latticy.module.counter;

import lombok.Data;

/**
 * 计数器对象
 */
@Data
public class Counter {

    // 计数值
    private int countNum ;

    // 最终计数值
    private int endNum ;

    // 递增数值
    private int increaseNum ;

    // 备用数字（防止重复）
    private int standby ;

    // 获取下一个缓存的数值
    public String getNextNum() {
        return String.valueOf(countNum += increaseNum);
    }

    // 判断当前缓存的数据是否使用完
    public boolean isUseUp() {
        return countNum >= endNum;
    }
}
