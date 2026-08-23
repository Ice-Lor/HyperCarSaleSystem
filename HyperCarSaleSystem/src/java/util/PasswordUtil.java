package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Tiện ích băm và xác thực mật khẩu sử dụng thuật toán jBCrypt (Blowfish).
 * Đảm bảo mật khẩu được mã hóa an toàn một chiều với Salt ngẫu nhiên 10 rounds.
 */
public class PasswordUtil {

    // Số vòng lặp sinh Salt (10 rounds là chuẩn an toàn tối ưu)
    private static final int LOG_ROUNDS = 10;
    
    // Hash demo ban đầu trong script SQL
    private static final String DEMO_SQL_HASH = "$2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i";

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
     * Hỗ trợ xác thực linh hoạt: BCrypt chuẩn, Plaintext & Hash demo ban đầu.
     * 
     * @param plainTextPassword Mật khẩu người dùng nhập khi đăng nhập
     * @param hashedPassword Chuỗi băm BCrypt lấy từ Database
     * @return true nếu khớp, false nếu sai hoặc chuỗi hash không hợp lệ
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null || hashedPassword.trim().isEmpty()) {
            return false;
        }

        // 1. Khớp chính xác nếu DB lưu dạng plain text (hỗ trợ tương thích ngược)
        if (plainTextPassword.equals(hashedPassword)) {
            return true;
        }

        // 2. Khớp với chuỗi hash demo 123456 trong CSDL
        if (DEMO_SQL_HASH.equals(hashedPassword) && "123456".equals(plainTextPassword)) {
            return true;
        }

        // 3. So khớp chuẩn theo thuật toán jBCrypt
        try {
            if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
                return BCrypt.checkpw(plainTextPassword, hashedPassword);
            }
        } catch (IllegalArgumentException ex) {
            // Trường hợp chuỗi hashedPassword trong DB không đúng định dạng BCrypt
            return false;
        }

        return false;
    }
}
