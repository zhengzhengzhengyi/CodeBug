package org.example.codereview.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 金额通用处理工具
 *
 * @author kongsiyu
 */
public class AmountUtils {

    /**
     * 运算位数
     */
    public static final int CALC_SCALE = 8;

    /**
     * 截取位数
     */
    public static final int ROUND_SCALE = 2;

    /**
     * 金额格式统一处理
     *
     * @return 2位小数
     */
    public static BigDecimal formatAmount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(ROUND_SCALE, RoundingMode.DOWN);
    }

    /**
     * 金额格式统一处理
     *
     * @return 8位小数
     */
    public static BigDecimal formatAmountEightBit(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(CALC_SCALE, RoundingMode.DOWN);
    }

    /**
     * 金额格式统一处理
     *
     * @return 8位小数 四舍五入
     */
    public static BigDecimal formatAmountEightBitUp(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(CALC_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 金额格式统一处理
     *
     * @return 2位小数 四舍五入
     */
    public static BigDecimal amountRounding(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(ROUND_SCALE, RoundingMode.HALF_UP);
    }



    private static <T> Map<Long, BigDecimal> calAmountGeneric(BigDecimal tax, List<T> items, Function<T, Integer> amountExtractor, Function<T, Long> idExtractor) {
        if (tax == null || items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        List<Integer> nums = items.stream().map(amountExtractor).collect(Collectors.toList());
        Map<Integer, Double> integerDoubleMap = calculatePercentages(nums);
        BigDecimal totalCalculatedTax = BigDecimal.ZERO;
        Map<Long, BigDecimal> result = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            BigDecimal resultTax;
            if (i == items.size() - 1) {
                resultTax = tax.subtract(totalCalculatedTax);
            } else {
                Double percentage = integerDoubleMap.getOrDefault(amountExtractor.apply(item), 0.0);
                resultTax = tax.multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP);
                totalCalculatedTax = totalCalculatedTax.add(resultTax);
            }
            result.put(idExtractor.apply(item), resultTax);
        }
        return result;
    }

    private static Map<Integer, Double> calculatePercentages(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }

        int totalSum = list.stream().mapToInt(Integer::intValue).sum();
        Map<Integer, Double> percentageMap = new HashMap<>();

        for (Integer number : list) {
            double percentage = ((double) number / totalSum);
            percentageMap.put(number, percentage);
        }

        return percentageMap;
    }


}