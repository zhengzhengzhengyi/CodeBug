package org.example.codereview.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wdp
 * @date 2024/7/12
 * @desc
 */
public class ClassCompareUtil {

    public static final Logger log = LoggerFactory.getLogger(ClassCompareUtil.class);

    /**
     * 旧值
     **/
    private static final String OLD_VALUE = "oldValue";

    /**
     * 新值
     **/
    private static final String NEW_VALUE = "newValue";


    /**
     * 对比两个对象的所有属性，返回变更的属性和属性值的映射
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    public static Map<String, Map<String, Object>> compareObjects(Object oldObject, Object newObject) {
        Map<String, Map<String, Object>> changes = new HashMap<>();
        if (oldObject == null || newObject == null) {
            return changes;
        }

        Class<?> oldClazz = oldObject.getClass();
        Class<?> newClazz = newObject.getClass();

        // 获取两个类的字段名集合，并计算交集
        Set<String> oldFields = Arrays.stream(oldClazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        Set<String> newFields = Arrays.stream(newClazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        oldFields.retainAll(newFields); // 直接取交集

        // 预加载字段映射
        Map<String, Field> oldFieldMap = Arrays.stream(oldClazz.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, f -> f));
        Map<String, Field> newFieldMap = Arrays.stream(newClazz.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, f -> f));

        // 遍历共有字段
        for (String fieldName : oldFields) {
            try {
                Field oldField = oldFieldMap.get(fieldName);
                Field newField = newFieldMap.get(fieldName);
                oldField.setAccessible(true);
                newField.setAccessible(true);

                Object oldValue = oldField.get(oldObject);
                Object newValue = newField.get(newObject);
                // 处理 BigDecimal 类型
                if (oldValue instanceof BigDecimal && newValue instanceof BigDecimal) {
                    BigDecimal oldBigDecimal = (BigDecimal) oldValue;
                    BigDecimal newBigDecimal = (BigDecimal) newValue;
                    boolean isChanged = oldBigDecimal.compareTo(newBigDecimal) != 0;
                    if (isChanged) {
                        Map<String, Object> diff = new HashMap<>();
                        diff.put(OLD_VALUE, oldBigDecimal.stripTrailingZeros().toPlainString());
                        diff.put(NEW_VALUE, newBigDecimal.stripTrailingZeros().toPlainString());
                        changes.put(fieldName, diff);
                    }
                } else {
                    // null 值处理
                    boolean isChanged = (oldValue == null && newValue != null) ||
                            (oldValue != null && newValue == null) ||
                            (oldValue != null && !oldValue.equals(newValue));

                    if (isChanged) {
                        Map<String, Object> diff = new HashMap<>();
                        diff.put(OLD_VALUE, oldValue);
                        diff.put(NEW_VALUE, newValue);
                        changes.put(fieldName, diff);
                    }
                }

            } catch (IllegalAccessException e) {
                log.error("Field access error: {}", fieldName, e);
            }
        }
        return changes;
    }

}

