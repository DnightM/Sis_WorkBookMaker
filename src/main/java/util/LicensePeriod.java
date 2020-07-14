package util;

import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LicensePeriod {
    public boolean limit(int year, int month, int day) throws Exception {
        LocalDateTime limit = LocalDateTime.of(year, month, day, 0, 0, 0);
        URLConnection conn = new URL("http://naver.com").openConnection();
        String gmt = conn.getHeaderFields().get("Date").get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        LocalDateTime l = LocalDateTime.parse(gmt, formatter).plusHours(9);
        if (l.isAfter(limit)) {
            return false;
        } else {
            return true;
        }
    }
}
