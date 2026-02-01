package com.xgz.cli.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void testParseAndFormatLocalDate() {
        LocalDate d = DateUtil.parseLocalDate("2026-01-31", DateUtil.PATTERN_DATE);
        assertEquals(LocalDate.of(2026, 1, 31), d);

        String s = DateUtil.format(d, DateUtil.PATTERN_DATE_COMPACT);
        assertEquals("20260131", s);
    }

    @Test
    void testParseAndFormatLocalTime() {
        LocalTime t = DateUtil.parseLocalTime("23:59:58", DateUtil.PATTERN_TIME);
        assertEquals(LocalTime.of(23, 59, 58), t);

        String s = DateUtil.format(t, DateUtil.PATTERN_TIME_COMPACT);
        assertEquals("235958", s);
    }

    @Test
    void testParseAndFormatLocalDateTime() {
        LocalDateTime dt = DateUtil.parseLocalDateTime("2026-01-31 12:13:14", DateUtil.PATTERN_DATETIME);
        assertEquals(LocalDateTime.of(2026, 1, 31, 12, 13, 14), dt);

        String s = DateUtil.format(dt, DateUtil.PATTERN_DATETIME_COMPACT);
        assertEquals("20260131121314", s);
    }

    @Test
    void testBlankInputReturnsNull() {
        assertNull(DateUtil.parseLocalDate(null, DateUtil.PATTERN_DATE));
        assertNull(DateUtil.parseLocalDate(" ", DateUtil.PATTERN_DATE));
        assertNull(DateUtil.parseLocalDateTime("\t", DateUtil.PATTERN_DATETIME));
    }

    @Test
    void testTryParseReturnsNullOnError() {
        assertNull(DateUtil.tryParseLocalDate("2026-99-99", DateUtil.PATTERN_DATE));
        assertNull(DateUtil.tryParseLocalTime("aa", DateUtil.PATTERN_TIME));
        assertNull(DateUtil.tryParseLocalDateTime("2026/01/31", DateUtil.PATTERN_DATETIME));
    }

    @Test
    void testDateConversionAndFormattingWithZone() {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalDateTime ldt = LocalDateTime.of(2026, 1, 31, 0, 0, 0);
        Date date = DateUtil.toDate(ldt, zone);

        assertEquals("2026-01-31 00:00:00", DateUtil.format(date, DateUtil.PATTERN_DATETIME, zone));

        // parseDate supports date-only patterns by defaulting time to 00:00:00
        Date parsed = DateUtil.parseDate("2026-01-31", DateUtil.PATTERN_DATE, zone, true);
        assertEquals("2026-01-31 00:00:00", DateUtil.format(parsed, DateUtil.PATTERN_DATETIME, zone));
    }
}
