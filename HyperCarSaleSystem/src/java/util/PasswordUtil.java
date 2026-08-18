package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Tiện ích mã hóa và kiểm tra mật khẩu bằng jBCrypt
 */
public class PasswordUtil {
    
    /**
     * Mã hoá mật khẩu dạng plain text thành chuỗi bCrypt hash
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }
    
    /**
     * Kiểm tra mật khẩu người dùng nhập với chuỗi hash trong CSDL
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
