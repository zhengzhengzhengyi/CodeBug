package org.example.codereview.util;


import io.micrometer.common.util.StringUtils;

/**
 * @description  金额大写转换工具类
 * @author
 */
public class AmountTransformUtils {
    /**
     * 繁体大写数字
     */
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /**
     * 繁体整数部分的单位
     */
    private static final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};
    /**
     * 繁体小数部分的单位 
     */
    private static final String[] DUNIT = {"角", "分"};
    /**
     * 简体数字
     */
    private static final String[] CN_NUMBERS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
 
    /**
     * 简体数字单位
     */
    private static final String[] CN_IUNIT = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
 
    private AmountTransformUtils() {
        throw new IllegalStateException("Utility class");
    }
 
    /**
     *  转换为大写的中文金额,支持负数
     * @param amount 金额
     * @param isSimplified 是否简体中文：true:简体，false：繁体
     * @return
     */
    public static String toChinese(String amount, boolean isSimplified) {
        // 判断输入的金额字符串是否符合要求
        if (StringUtils.isBlank(amount) || !amount.matches("(-)?[\\d]*(.)?[\\d]*")) {
            throw new RuntimeException("请输入数字");
        }
 
        if ("0".equals(amount) || "0.00".equals(amount) || "0.0".equals(amount)) {
            return isSimplified ? "零" : "零元";
        }
 
        // 判断金额数字中是否存在负号"-"
        boolean flag = false;
        if (amount.startsWith("-")) {
            // 标志位，标志此金额数字为负数
            flag = true;
            amount = amount.replaceAll("-", "");
        }
        // 去掉金额数字中的逗号","
        amount = amount.replaceAll(",", "");
        // 初始化：分离整数部分和小数部分
        String[] separateNum = separateNum(amount);
        // 整数部分数字
        String integerStr = separateNum[0];
        // 小数部分数字
        String decimalStr = separateNum[1];
        // beyond超出计算能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            throw new RuntimeException("输入数字超限");
        }
        // 整数部分数字
        int[] integers = toIntArray(integerStr);
        // 判断整数部分是否存在输入012的情况
        if (integers.length > 1 && integers[0] == 0) {
            throw new RuntimeException("输入数字不符合要求");
        }
        // 设置万单位
        boolean isWan = isWan5(integerStr);
        // 小数部分数字
        int[] decimals = toIntArray(decimalStr);
        // 返回最终的大写金额
        String result = "";
        String chineseInteger = getChineseInteger(integers, isWan, isSimplified);
        String chineseDecimal = getChineseDecimal(decimals, isSimplified);
        if (decimals.length > 0 && isSimplified) {
            result = chineseInteger;
            if (!chineseDecimal.equals("零零")) {
                result = result + "点" + chineseDecimal;
            }
        } else {
            result = chineseInteger + chineseDecimal;
 
        }
        if (flag) {
            // 如果是负数，加上"负"
            return "负" + result;
        } else {
            return result;
        }
    }
 
    /**
     * 分离整数部分和小数部分
     * @param str
     * @return
     */
    private static String[] separateNum(String str) {
        String integerStr;// 整数部分数字
        String decimalStr;// 小数部分数字
        if (str.indexOf('.') >= 1) {
            integerStr = str.substring(0, str.indexOf('.'));
            decimalStr = str.substring(str.indexOf('.') + 1);
            if (decimalStr.length() > 2) {
                decimalStr = decimalStr.substring(0, 2);
            }
        } else if (str.indexOf('.') == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }
        return new String[] {integerStr, decimalStr};
    }
 
    /**
     *  将字符串转为int数组
     * @param number  数字
     * @return
     */
    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }
 
    /**
     *  将整数部分转为大写的金额
     * @param integers 整数部分数字
     * @param isWan  整数部分是否已经是达到【万】
     * @return
     */
    private static String getChineseInteger(int[] integers, boolean isWan, boolean isSimplified) {
 
        int length = integers.length;
        if (!isSimplified && length == 1 && integers[0] == 0) {
            return "";
        }
        if (!isSimplified) {
            return traditionalChineseInteger(integers, isWan);
        } else {
            return simplifiedChineseInteger(integers, isWan);
        }
    }
 
    /**
     * 繁体中文整数
     * @param integers
     * @param isWan
     * @return
     */
    private static String traditionalChineseInteger(int[] integers, boolean isWan) {
        StringBuilder chineseInteger = new StringBuilder("");
        int length = integers.length;
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)// 万（亿）
                    key = IUNIT[4];
                else if ((length - i) == 9) {// 亿
                    key = IUNIT[8];
                } else if ((length - i) == 5 && isWan) {// 万
                    key = IUNIT[4];
                } else if ((length - i) == 1) {// 元
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }
 
    /**
     * 简体中文整数
     * @param integers
     * @param isWan
     * @return
     */
    private static String simplifiedChineseInteger(int[] integers, boolean isWan) {
        StringBuilder chineseInteger = new StringBuilder("");
        int length = integers.length;
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13) {// 万（亿）
                    key = CN_IUNIT[4];
                } else if ((length - i) == 9) {// 亿
                    key = CN_IUNIT[8];
                } else if ((length - i) == 5 && isWan) {// 万
                    key = CN_IUNIT[4];
                } else if ((length - i) == 1) {// 元
                    key = CN_IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += CN_NUMBERS[0];
                }
                if (length == 1 && integers[i] == 0) {
                    key += CN_NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (CN_NUMBERS[integers[i]] + CN_IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }
 
    /**
     *  将小数部分转为大写的金额
     * @param decimals 小数部分的数字
     * @return
     */
    private static String getChineseDecimal(int[] decimals, boolean isSimplified) {
        StringBuilder chineseDecimal = new StringBuilder("");
        if (!isSimplified) {
            for (int i = 0; i < decimals.length; i++) {
                String key = "";
 
                if ((decimals.length - i) > 1 && decimals[i + 1] != 0) {
                    key += NUMBERS[0];
                }
 
                chineseDecimal.append(decimals[i] == 0 ? key : (NUMBERS[decimals[i]] + DUNIT[i]));
            }
        } else {
            for (int i = 0; i < decimals.length; i++) {
                chineseDecimal.append(CN_NUMBERS[decimals[i]]);
            }
 
        }
        return chineseDecimal.toString();
    }
 
    /**
     *  判断当前整数部分是否已经是达到【万】
     * @param integerStr  整数部分数字
     * @return
     */
    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }
 
    public static void main(String[] args) {
        String amount = "123456789012.123";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        amount = "0.123";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        amount = "0";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        amount = "-1123456789.123";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        amount = "120001.01";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        amount = "120001.00";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, true));
        System.out.println("=================");
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
        amount = "0.123";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
        amount = "0";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
        amount = "-1123456789.123";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
        amount = "120001.01";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
        amount = "120001.00";
        System.out.println(amount + ":" + AmountTransformUtils.toChinese(amount, false));
    }
}