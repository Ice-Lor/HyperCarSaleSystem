package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Tiện ích băm và xác thực mật khẩu sử dụng thuật toán jBCrypt (Blowfish).
 * Đảm bảo mật khẩu được mã hóa an toàn một chiều với Salt ngẫu nhiên 10 rounds.
 */
public class PasswordUtil {

    // Số vòng lặp sinh Salt (10 rounds là chuẩn an toàn tối ưu)
    private static final int LOG_ROUNDS = 10;

    /**
     * Băm mật khẩu dạng plain text thành chuỗi mã hóa BCrypt kèm Salt.
     * 
     * @param plainTextPassword Mật khẩu người dùng nhập
     * @return Chuỗi băm BCrypt ($2a$10$...)
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * So khớp mật khẩu dạng plain text với chuỗi mã hóa BCrypt đã lưu trong DB.
     * 
     * @param plainTextPassword Mật khẩu người dùng nhập khi đăng nhập
     * @param hashedPassword Chuỗi băm BCrypt lấy từ Database
     * @return true nếu khớp, false nếu sai hoặc chuỗi hash không hợp lệ
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null || hashedPassword.trim().isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException ex) {
            // Trường hợp chuỗi hashedPassword trong DB không đúng định dạng BCrypt
            return false;
        }
    }
}
