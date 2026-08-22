package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Tiện ích định dạng tiền tệ, ngày tháng và số hiển thị trên toàn hệ thống Showroom.
 */
public class FormatUtil {

    private static final DecimalFormat CURRENCY_FORMAT;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        CURRENCY_FORMAT = new DecimalFormat("$#,##0.00", symbols);
    }

    /**
     * Định dạng số tiền BigDecimal thành dạng tiền tệ USD chuẩn quốc tế ($3,800,000.00).
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Định dạng số tiền double thành dạng tiền tệ USD ($3,800,000.00).
     */
    public static String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Định dạng ngày tháng dạng dd/MM/yyyy (ví dụ: 22/08/2026).
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }

    /**
     * Định dạng ngày giờ dạng dd/MM/yyyy HH:mm (ví dụ: 22/08/2026 14:30).
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_FORMAT.format(date);
    }
}
