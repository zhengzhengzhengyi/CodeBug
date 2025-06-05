package org.example.codereview.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author wdp
 * @desc
 * @date 2024/9/11
 */
public class BigDecimalUtils {

    public static final BigDecimal ONE_HUNDRED_THOUSAND = new BigDecimal("1000000");

    public static final BigDecimal HUNDRED = new BigDecimal("100");
    private BigDecimalUtils() {

    }

    /**
     * 检查是否是整数
     * @param v
     * @return
     */
    public static boolean isIntegerValue(BigDecimal v) {
        boolean ret;
        try {
            v.toBigIntegerExact();
            ret = true;
        } catch (ArithmeticException ex) {
            ret = false;
        }
        return ret;
    }

    /**
     * 校验BigDecimal是否为空或等于0
     * @param vals
     * @return
     */
    public static boolean decimalAccordRequirements(BigDecimal... vals) {
        for (BigDecimal val : vals) {
            if (val == null || Objects.equals(val, BigDecimal.ZERO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转BigDecimal
     * @param str
     * @return
     */
    public static BigDecimal valueOf(String str) {
        if (str == null || str.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + str, e);
        }
    }

    /**
     * 除法
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, Integer scale) {
        if (v1 == null) {
            v1 = BigDecimal.ZERO;
        }
        if (v2 == null) {
            v2 = BigDecimal.ZERO;
        }
        if (v2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return v1.divide(v2, scale, RoundingMode.HALF_UP);
        }
    }

    public static BigDecimal calcPlannedPaymentAmount(BigDecimal unitPriceIncludingTax, BigDecimal advanceRatio, Integer quantity) {
        return BigDecimal.valueOf(quantity)
                .multiply(unitPriceIncludingTax)
                .multiply(BigDecimal.ONE.subtract(advanceRatio
                        .divide(BigDecimalUtils.HUNDRED, 2, RoundingMode.HALF_UP)));
    }



    public static BigDecimal roundTwoDecimalPlaces(BigDecimal value){
        BigDecimal bigDecimal = new BigDecimal("0");
        if (value != null) {
            BigDecimal number = value.setScale(2, BigDecimal.ROUND_HALF_UP);
            String format = String.format("%.2f", number);
            bigDecimal= new BigDecimal(format);
            return bigDecimal;
        }

        return bigDecimal;
    }


}
