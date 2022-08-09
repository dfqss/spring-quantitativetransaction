package io.github.talelin.latticy.bo;

import io.github.talelin.latticy.common.constant.FileLogoConstant;
import io.github.talelin.latticy.model.*;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntityBo {

    private String entityName ;

    private Class<?> entityClass ;

    private String[] entitySetMethods ;

    /**
     * 无参构造器
     */
    public EntityBo() { super(); }

    /**
     * 形参构造器
     * @param entityName
     * @param entityClass
     */
    public EntityBo(String entityName, Class<?> entityClass) {
        this.entityClass = entityClass;
        this.entityName = entityName;
        init();
    }

    /**
     * 初始化实体名称
     */
    private void init() {
        if(FinAnalysisIndexDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.FIN_ANALYSIS_INDEX_ARR;
        }else if(GrowthIndexDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.GROWTH_INDEX_ARR;
        }else if(DupontAnalysisIndexDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.DUPONT_ANALYSIS_INDEX_ARR;
        }else if(StockValueDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.STOCK_VALUE_ARR;
        }else if(TecAnalysisIndexDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.TEC_ANALYSIS_INDEX_ARR;
        }else if(IndustryClassDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.INDUSTRY_CLASS_ARR;
        }else if(SecBasicIndexDO.class.equals(this.entityClass)) {
            this.entitySetMethods = FileLogoConstant.SEC_BASIC_INDEX_ARR;
        }
        initSetMethods();
    }

    /**
     * 初始化投资指标set方法列表(无序的)
     */
    private void initSetMethods() {
        if(this.entitySetMethods == null || this.entitySetMethods.length <= 0) {
            Method[] invokeMethods = this.entityClass.getDeclaredMethods();
            List<String> setMethodsList = new ArrayList<>();
            for(Method invokeMethod : invokeMethods) {
                if(invokeMethod.getName().startsWith(FileLogoConstant.PREFIX_SET)) {
                    setMethodsList.add(invokeMethod.getName());
                }
            }
            this.entitySetMethods = setMethodsList.toArray(new String[setMethodsList.size()]);
        }
    }
}
