package org.example.codereview.util;

import cn.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

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

    // 7.
    public static void threadUnsafe() {
        List<Integer> list = new ArrayList<>();
        IntStream.range(0, 1000).parallel().forEach(list::add); // 并发修改
    }

    // 8.
    public static void numberFormat() {
        Integer.parseInt("not a number"); // NumberFormatException
    }

    // 9.
    public static void arrayOutOfBounds() {
        int[] arr = new int[5];
        arr[10] = 1; // ArrayIndexOutOfBoundsException
    }


}
