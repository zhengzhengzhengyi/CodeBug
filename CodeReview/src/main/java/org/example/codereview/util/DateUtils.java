package org.example.codereview.util;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;
import java.util.function.ToLongFunction;

/**
 * @author Administrator
 */
public class DateUtils {

    private static final EnumMap<TimeUnit, ToLongFunction<Duration>> ZONED_DATE_TIME_UNIT_HANDLERS = new EnumMap<>(TimeUnit.class);

    static {
        ZONED_DATE_TIME_UNIT_HANDLERS.put(TimeUnit.MINUTES, Duration::toMinutes);
        ZONED_DATE_TIME_UNIT_HANDLERS.put(TimeUnit.HOURS, Duration::toHours);
        ZONED_DATE_TIME_UNIT_HANDLERS.put(TimeUnit.DAYS, Duration::toDays);
        ZONED_DATE_TIME_UNIT_HANDLERS.put(TimeUnit.MILLISECONDS, Duration::toMillis);
        ZONED_DATE_TIME_UNIT_HANDLERS.put(TimeUnit.NANOSECONDS, Duration::toNanos);
    }

    public static String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMM", "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy年MM月dd日", "yyMMdd", "MM/dd/yyyy", "MM/yyyy", "HH:mm:ss","yyyy"};

    /**
     * 解析yyyy-MM-dd 格式的日期
     * @param onlyDateStr
     * @return
     */
    public static ZonedDateTime parseOnlyDateStr(String onlyDateStr){
        return ZonedDateTime.parse(onlyDateStr+" "+"00:00:00", DateTimeFormatter.ofPattern(PARSE_PATTERNS[1]).withZone(ZoneId.of("Asia/Shanghai")));
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式的时间
     * @param time
     * @return
     */
    public static ZonedDateTime parseStr(String time){
        return ZonedDateTime.parse(time, DateTimeFormatter.ofPattern(PARSE_PATTERNS[1]).withZone(ZoneId.of("Asia/Shanghai")));
    }

    /**
     * zonedDateTime格式化
     * @param datetime
     * @return
     */
    public static String zonedDateFormat(ZonedDateTime datetime) {
        return datetime.format(DateTimeFormatter.ofPattern(PARSE_PATTERNS[1]));
    }

    public static String zonedDateFormat(ZonedDateTime datetime, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return zonedDateFormat(datetime);
        }
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当天零点零分零秒
     * @param zonedDateTime
     * @return
     */
    public static ZonedDateTime getStartOfDay(ZonedDateTime zonedDateTime){
        return zonedDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * Date 转 字符串
     * @param format
     * @param date
     * @return
     */
    public static String formatDateToStr(final String format, final Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     *  字符串转 Date
     * @param str
     * @param format
     * @return
     */
    public static Date parseToDate(String str, String format) {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date addDate(Date date, int field, int amount) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(field, amount);
        return cl.getTime();
    }

    /**
     *  两个日期相差的月份
     * @param startDate
     * @param currentDate
     * @return
     */
    public static long getDiffMonth(LocalDate startDate, LocalDate currentDate) {
        // 使用每个月的第一天作为比较的起点
        return ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), currentDate.withDayOfMonth(1));
    }

    /**
     * 获取两个时间相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDiffDays(Date date1, Date date2) {
        if (ObjectUtil.isNull(date1) || ObjectUtil.isNull(date2)) {
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        long millis1 = c1.getTimeInMillis();
        long millis2 = c2.getTimeInMillis();
        long diff = Math.abs(millis2 - millis1);
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static int getDiffDaysNoAbs(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        long millis1 = c1.getTimeInMillis();
        long millis2 = c2.getTimeInMillis();
        long diff = millis1 - millis2;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static Date convertZonedDateTimeToDate(ZonedDateTime zonedDateTime) {
        // 将 ZonedDateTime 转换成 Instant
        Instant instant = zonedDateTime.toInstant();
        // 将 Instant 转换成 Date
        return Date.from(instant);
    }
    /**
     * 查找计算最大日期
     * @param dates
     * @return
     */
    public static Date calcMaxDate(Date ... dates) {
        if (dates == null || dates.length == 0) {
            return null;
        }

        Date maxDate = dates[0];
        for (Date date : dates) {
            if (date != null && maxDate.compareTo(date) < 0) {
                maxDate = date;
            }
        }
        return maxDate;
    }

    /**
     * 校验是否兼容格式  yyyy-MM-dd yyyy/MM/dd
     * @param dateStr
     * @return
     */
    public static Date compatibleYYYYMMDD(String dateStr) {
        Date date = DateUtils.parseToDate(dateStr, DateUtils.PARSE_PATTERNS[0]);
        if (date == null) {
            date = DateUtils.parseToDate(dateStr, DateUtils.PARSE_PATTERNS[4]);
        }
        return date;
    }

    /**
     * 校验是否兼容格式  yyyy-MM yyyy/MM
     * @param month
     * @return
     */
    public static String compatibleYYYYMM(String month) {
        Date date = DateUtils.parseToDate(month, DateUtils.PARSE_PATTERNS[3]);
        if (date == null) {
            date = DateUtils.parseToDate(month, DateUtils.PARSE_PATTERNS[7]);
        }
        String dateStr = "";
        if (date != null) {
            dateStr = DateUtils.formatDateToStr(DateUtils.PARSE_PATTERNS[3], date);
        }
        return dateStr;
    }

    /**
     * 日期是否是当天
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(PARSE_PATTERNS[0]);
        sdf.setLenient(false);
        String inputDateStr = sdf.format(date);
        String todayStr = sdf.format(new Date());

        return inputDateStr.equals(todayStr);
    }

    /**
     * 校验日期是否有效
     * @param dateStr
     * @param formats
     * @return
     */
    public static boolean isValidDate(String dateStr, String ... formats) {
        for (String format : formats) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            try {
                LocalDate.parse(dateStr, dateFormatter);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * date 转 ZonedDateTime
     * @param date
     * @return
     */
    public static ZonedDateTime convertDateToZonedDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault());
    }

    public static long zonedDateTimeBetween(ZonedDateTime time1, ZonedDateTime time2, TimeUnit unit, boolean abs) {
        // 参数校验
        if (time1 == null || time2 == null || unit == null) {
            throw new RuntimeException("参数不能为空");
        }
        // 如果单位是天，则截断到当天的开始时间
        if (unit == TimeUnit.DAYS) {
            time1 = time1.truncatedTo(ChronoUnit.DAYS);
            time2 = time2.truncatedTo(ChronoUnit.DAYS);
        }
        // 计算时差
        Duration durationBetween = Duration.between(time1, time2);

        // 获取对应的计算方法
        ToLongFunction<Duration> handler = ZONED_DATE_TIME_UNIT_HANDLERS.get(unit);
        if (handler == null) {
            throw new RuntimeException("不支持的时间单位: " + unit);
        }
        return abs ? Math.abs(handler.applyAsLong(durationBetween)) : handler.applyAsLong(durationBetween);
    }
    /**
     * Date转localDate
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * localDate转Date
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化localDate
     * @param localDate
     * @param formatPattern
     * @return
     */
    public static String formatLocalDate(LocalDate localDate, String formatPattern) {
        // 创建 DateTimeFormatter 对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);

        // 使用指定格式格式化 LocalDate
        return localDate.format(formatter);
    }



    public static void main(String[] args) {
        // 创建两个不同时区的 ZonedDateTime 对象
        ZonedDateTime time1 = ZonedDateTime.of(2025, 3, 14, 9, 0, 7, 0, ZoneId.systemDefault());
        ZonedDateTime time2 = ZonedDateTime.of(2025, 3, 11, 8, 56, 42, 0, ZoneId.systemDefault());

        // 计算两个时间之间的时差
        long duration = zonedDateTimeBetween(time1, time2, TimeUnit.DAYS, true);
        System.out.println("duration:" + duration);
    }

}
