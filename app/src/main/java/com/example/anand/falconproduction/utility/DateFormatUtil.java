package com.example.anand.falconproduction.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by anand on 4/2/15.
 *
 * This class is copy of Baluc code for determining date format from string.
 * Link to original class - http://balusc.blogspot.in/2007/09/dateutil.html
 */
public class DateFormatUtil {

  private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
    put("^\\d{8}$", "yyyyMMdd");
    put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
    put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
    put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
    put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
    put("^\\d{12}$", "yyyyMMddHHmm");
    put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
    put("^\\d{14}$", "yyyyMMddHHmmss");
    put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}.\\d{1,3}$", "yyyy-MM-dd HH:mm:ss.SSS");
    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
  }};

  private DateFormatUtil() {

  }

  public static String determineDateFormat(String dateString) {
    for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
      if (dateString.toLowerCase().matches(regexp)) {
        return DATE_FORMAT_REGEXPS.get(regexp);
      }
    }
    return null; // Unknown format.
  }

  public static Date parse(String dateString, String dateFormat) throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    simpleDateFormat.setLenient(false); // Don't automatically convert invalid date.
    return simpleDateFormat.parse(dateString);
  }

  public static Date parse(String dateString) throws ParseException {
    String dateFormat = determineDateFormat(dateString);
    if (dateFormat == null) {
      throw new ParseException("Unknown date format.", 0);
    }
    return parse(dateString, dateFormat);
  }

}