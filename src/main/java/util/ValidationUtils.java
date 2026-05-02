package util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,20}$");
    private static final String PHONE_PATTERN = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        return Pattern.compile(PHONE_PATTERN).matcher(phone.trim()).matches();
    }

    public static boolean isStrongPassword(String password) {
        if (!isNotEmpty(password)) return false;
        return Pattern.compile(PASSWORD_PATTERN).matcher(password.trim()).matches();
    }

    public static boolean isAlphanumeric(String str) {
        if (!isNotEmpty(str)) return false;
        return Pattern.compile("^[a-zA-Z0-9]+$").matcher(str.trim()).matches();
    }

    public static boolean isLetterAndSpace(String str) {
        if (!isNotEmpty(str)) return false;
        return Pattern.compile("^[\\p{L}\\s]+$").matcher(str.trim()).matches();
    }
}
