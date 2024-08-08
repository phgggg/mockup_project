package com.itsol.mockup.utils;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author anhvd_itsol
 */

public class DataUtils {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataUtils.class);

    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    public static boolean isNullOrEmpty(String value) {
        return (value == null || value.isEmpty());
    }

    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.compareTo(BigDecimal.ZERO) == 0);
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        Long result = defaultValue;
        if (obj1 != null) {
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Long safeToLong(Object obj1) {
        Long result = null;
        if (obj1 != null) {
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static BigDecimal safeToBigDecimal(Object obj1) {
        BigDecimal result = new BigDecimal(0);
        if (obj1 != null) {
            try {
                result = BigDecimal.valueOf(Long.parseLong(obj1.toString()));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Double safeToDouble(Object obj1) {
        Double result = null;
        if (obj1 != null) {
            try {
                result = Double.parseDouble(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Short safeToShort(Object obj1) {
        Short result = 0;
        if (obj1 != null) {
            try {
                result = Short.parseShort(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static int safeToInt(Object obj1) {
        int result = 0;
        if (obj1 == null) {
            return 0;
        }
        try {
            result = Integer.parseInt(obj1.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static String safeToString(Object obj1) {
        if (obj1 == null) {
            return "";
        }

        return obj1.toString();
    }

    public static Date safeToDate(Object obj1) {
        Date result = null;
        if (obj1 == null) {
            return null;
        }
        try {
            result = (Date) obj1;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public static boolean isNumber(String strId) {
        return strId.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isLong(String strId) {
        return strId.matches("\\d*");
    }

    public static boolean isNullOrZero(Short value) {
        return (value == null || value.equals(Short.parseShort("0")));
    }

    public static boolean isNullOrZero(Integer value) {
        return (value == null || value.equals(Integer.parseInt("0")));
    }

    public static long dayDiff(Date a, Date b){
        return TimeUnit.DAYS.convert(a.getTime() - b.getTime(), TimeUnit.MILLISECONDS);
    }

    public static long getDateDiff(Timestamp oldTs, Timestamp newTs, TimeUnit timeUnit) {
        long diffInMS = newTs.getTime() - oldTs.getTime();
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS);
    }

    public static String escapeSpecialChars(String input) {
        return input.trim()
                .replace("\\", "\\\\")
                .replaceAll("%", "\\%")
                .replaceAll("_", "\\_");
    }

    public static int getMonthFromTimestamp(Timestamp timestamp){
        Calendar calendar = Calendar.getInstance();
        if(timestamp==null){
            return -1;
        }
        calendar.setTime(timestamp);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Timestamp getDayOfWeek(Timestamp timestamp, DayOfWeek dayOfWeek) {
        LocalDate date = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // Adjust to get the specific day of the week
        return convertLocalDateToTimestamp(date.with(dayOfWeek));
    }

    public static Timestamp convertLocalDateToTimestamp(LocalDate localDate) {
        // Combine LocalDate with a default LocalTime
        LocalDateTime localDateTime = localDate.atStartOfDay();

        // Convert to Instant using the system default time zone
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // Convert Instant to Timestamp
        return Timestamp.from(instant);
    }

}
