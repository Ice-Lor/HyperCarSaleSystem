package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Tiện ích định dạng số tiền, mã lực, tốc độ siêu xe
 */
public class FormatUtil {

    private static final DecimalFormat CURRENCY_USD = new DecimalFormat("$#,##0");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,##0");

    public static String formatUSD(double amount) {
        return CURRENCY_USD.format(amount);
    }

    public static String formatNumber(double number) {
        return NUMBER_FORMAT.format(number);
    }
}
