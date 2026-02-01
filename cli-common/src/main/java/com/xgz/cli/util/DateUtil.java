package com.xgz.cli.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;

/**
 * 日期、时间工具类
 *
 * <p>基于 JDK8 {@code java.time} 实现，线程安全。</p>
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
public class DateUtil {

    private DateUtil() {
        // util class
    }

    /** yyyy-MM-dd */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    /** yyyyMMdd */
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";

    /** HH:mm:ss */
    public static final String PATTERN_TIME = "HH:mm:ss";
    /** HHmmss */
    public static final String PATTERN_TIME_COMPACT = "HHmmss";

    /** yyyy-MM-dd HH:mm:ss */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /** yyyyMMddHHmmss */
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";

    /** yyyy-MM-dd'T'HH:mm:ss */
    public static final String PATTERN_DATETIME_ISO_LOCAL = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 默认使用系统时区。
     */
    public static ZoneId systemZone() {
        return ZoneId.systemDefault();
    }

    // ---------------------- parse: String -> LocalDate / LocalTime / LocalDateTime ----------------------

    public static LocalDate parseLocalDate(String text, String pattern) {
        return parseLocalDate(text, pattern, true);
    }

    public static LocalDate parseLocalDate(String text, String pattern, boolean strict) {
        if (isBlank(text)) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        DateTimeFormatter formatter = formatter(pattern, strict);
        return LocalDate.parse(text.trim(), formatter);
    }

    public static LocalTime parseLocalTime(String text, String pattern) {
        return parseLocalTime(text, pattern, true);
    }

    public static LocalTime parseLocalTime(String text, String pattern, boolean strict) {
        if (isBlank(text)) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        DateTimeFormatter formatter = formatter(pattern, strict);
        return LocalTime.parse(text.trim(), formatter);
    }

    public static LocalDateTime parseLocalDateTime(String text, String pattern) {
        return parseLocalDateTime(text, pattern, true);
    }

    public static LocalDateTime parseLocalDateTime(String text, String pattern, boolean strict) {
        if (isBlank(text)) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        DateTimeFormatter formatter = formatter(pattern, strict);
        return LocalDateTime.parse(text.trim(), formatter);
    }

    // ---------------------- tryParse: swallow parse exception, return null ----------------------

    public static LocalDate tryParseLocalDate(String text, String pattern) {
        try {
            return parseLocalDate(text, pattern);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static LocalTime tryParseLocalTime(String text, String pattern) {
        try {
            return parseLocalTime(text, pattern);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static LocalDateTime tryParseLocalDateTime(String text, String pattern) {
        try {
            return parseLocalDateTime(text, pattern);
        } catch (RuntimeException e) {
            return null;
        }
    }

    // ---------------------- format: LocalDate / LocalTime / LocalDateTime -> String ----------------------

    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    // ---------------------- conversion: Date / Instant / LocalDateTime ----------------------

    public static Instant toInstant(Date date) {
        return date == null ? null : date.toInstant();
    }

    public static Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, systemZone());
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        ZoneId zone = zoneId != null ? zoneId : systemZone();
        return LocalDateTime.ofInstant(date.toInstant(), zone);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return toDate(localDateTime, systemZone());
    }

    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zone = zoneId != null ? zoneId : systemZone();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    // ---------------------- shortcut: String <-> Date ----------------------

    public static Date parseDate(String text, String pattern) {
        return parseDate(text, pattern, systemZone(), true);
    }

    public static Date parseDate(String text, String pattern, ZoneId zoneId, boolean strict) {
        if (isBlank(text)) {
            return null;
        }
        LocalDateTime ldt;
        try {
            // 优先按 LocalDateTime 解析（pattern 包含时间时）
            ldt = parseLocalDateTime(text, pattern, strict);
        } catch (DateTimeParseException ex) {
            // 再尝试按 LocalDate 解析（pattern 仅日期时）
            LocalDate ld;
            try {
                ld = parseLocalDate(text, pattern, strict);
            } catch (DateTimeParseException ex2) {
                // 两种解析都失败时，保留第一次异常，便于定位问题
                throw ex;
            }
            if (ld == null) {
                return null;
            }
            ldt = ld.atStartOfDay();
        }
        return toDate(ldt, zoneId);
    }

    public static String format(Date date, String pattern) {
        return format(date, pattern, systemZone());
    }

    public static String format(Date date, String pattern, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("pattern is blank");
        }
        ZoneId zone = zoneId != null ? zoneId : systemZone();
        return LocalDateTime.ofInstant(date.toInstant(), zone).format(DateTimeFormatter.ofPattern(pattern));
    }

    // ---------------------- internal helpers ----------------------

    private static DateTimeFormatter formatter(String pattern, boolean strict) {
        // STRICT 模式下，`yyyy` + YearOfEra 可能无法组装出 LocalDate/LocalDateTime（需要 era 信息）。
        // 使用 `uuuu`（proleptic-year）更适合做严格意义的年月日解析。
        String p = pattern.contains("yyyy") ? pattern.replace("yyyy", "uuuu") : pattern;
        DateTimeFormatter base = DateTimeFormatter.ofPattern(p);
        return strict ? base.withResolverStyle(ResolverStyle.STRICT) : base.withResolverStyle(ResolverStyle.SMART);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
