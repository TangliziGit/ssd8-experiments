package me.tanglizi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * TimeUtil, provides some useful static method for time formatting and parsing functions.
 *
 * @author Zhang Chunxu
 */
public class TimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String format(Instant time) {
        return dateFormat.format(Date.from(time));
    }

    public static Date parse(String line) throws ParseException {
        return dateFormat.parse(line);
    }
}
