package org.example.codereview.util;

import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenkangping
 */
public class BeanConvertUtils {
    /**
     * 方法说明：将bean转化为另一种bean实体
     *  
     *
     * @param object
     * @param entityClass
     * @return
     */
    public static <T> T convertBean(Object object, Class<T> entityClass) {
        if (null == object) {
            return null;
        }
        return JSONUtil.toBean(JSONUtil.toJsonStr(object), entityClass);
    }


    /**
     * 方法说明：对象转换
     *  
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     * @return
     */
    public static <T> T copy(Object source, Class<T> target, String... ignoreProperties) {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, targetInstance);
        } else {
            BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
        }
        return targetInstance;
    }

    /**
     * 方法说明：对象转换(List)
     *  
     *
     * @param list             原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     * @return
     */
    public static <T, E> List<T> copyList(List<E> list, Class<T> target, String... ignoreProperties) {
        List<T> targetList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return targetList;
        }
        for (E e : list) {
            targetList.add(copy(e, target, ignoreProperties));
        }
        return targetList;
    }

    /**
     * 方法说明：对象转化为Map
     *  
     *
     * @param object
     * @return
     */
    public static Map<?, ?> objectToMap(Object object) {
        return convertBean(object, Map.class);
    }
}
