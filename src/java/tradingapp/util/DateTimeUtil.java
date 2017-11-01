package tradingapp.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

  public static String getNumberedDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return getTwoDigitDayOrMonthNumber(calendar.get(Calendar.DAY_OF_MONTH))
        + getTwoDigitDayOrMonthNumber(calendar.get(Calendar.MONTH)+1) + getTwoDigitDayOrMonthNumber(calendar.get(Calendar.YEAR));
  }

  public static String getTwoDigitDayOrMonthNumber(int input) {
    return input < 10 ? "0" + input : input + "";
  }
  
  public static boolean isWeekendDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
      return true;
    }
    return false;
  }
}
