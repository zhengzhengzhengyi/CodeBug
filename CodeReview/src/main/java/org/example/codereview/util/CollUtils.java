package org.example.codereview.util;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 集合工具类
 */
public class CollUtils {

    /**
     * 将集合转成Map
     * @param collection 要转成Map的集合
     * @param keyGetter 生成key的函数
     * @param valueGetter 生成value的函数
     * @return
     * @param <E>
     * @param <T>
     * @param <R>
     */
    public static <E,T,R> Map<T,R> toMap(Collection<E> collection, Function<E,T> keyGetter,Function<E,R> valueGetter){
        if(CollUtil.isEmpty(collection)){
            return Collections.emptyMap();
        }
        Map<T,R> result=new HashMap<>();
        for(E e:collection){
            T key = keyGetter.apply(e);
            R value=valueGetter.apply(e);
            result.put(key,value);
        }
        return result;
    }

}
