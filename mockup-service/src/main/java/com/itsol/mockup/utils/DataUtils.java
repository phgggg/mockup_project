package com.itsol.mockup.utils;

import com.itsol.mockup.entity.TimeSheetEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

    public static String TaskComment(TimeSheetEntity timeSheetEntity) {
        String result = null;
        String level = "";
        String status = "";

        if(timeSheetEntity.getStatus() == 0) result = "Task is pending";
        else if (timeSheetEntity.getStatus() == 1) {
            result = "Task is on going";
        } else if (timeSheetEntity.getStatus() == 2) {
            long diff = DataUtils.getDateDiff(timeSheetEntity.getActualFinishDate(), timeSheetEntity.getFinishDateExpected(), TimeUnit.DAYS);
            switch (Math.abs((int) diff)) {
                case 0:
                case 1:
                    level += " ";
                    break;
                case 2:
                case 3:
                    level += " pretty ";
                    break;
                case 4:
                case 5:
                    level += " highly ";
                default:
                    level += " extremely ";
            }
            status = (diff < 0) ? "late" : "early";
            if (diff == 0) status = "on time";
            result = ", Task is done" + level + status;
        }
        return result;
    }

}
