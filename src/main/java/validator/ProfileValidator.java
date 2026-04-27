package validator;

import java.util.HashMap;
import java.util.Map;

import util.ValidationUtils;

public class ProfileValidator {

    public Map<String, String> validate(String fullName, String phone) {
        Map<String, String> errors = new HashMap<>();

        // Validate Full Name
        if (!ValidationUtils.isNotEmpty(fullName)) {
            errors.put("fullName", "Họ và tên không được để trống.");
        } else if (!ValidationUtils.isLetterAndSpace(fullName)) {
            errors.put("fullName", "Họ và tên không được chứa số hoặc kí tự đặc biệt");
        }

        // Validate Phone Number
        if (ValidationUtils.isNotEmpty(phone)) {
            if (!ValidationUtils.isValidPhone(phone)) {
                errors.put("phone", "Số điện thoại không đúng định dạng.");
            }
        }
        return errors;
    }

    public Map<String, String> validatePasswordChange(String oldPass, String actualOldPass, String newPass, String confirmPass) {
        Map<String, String> errors = new HashMap<>();

        if (!ValidationUtils.isNotEmpty(oldPass)) {
            errors.put("oldPassword", "Mật khẩu hiện tại không được để trống.");
        } else if (!oldPass.equals(actualOldPass)) {
            errors.put("oldPassword", "Mật khẩu hiện tại không chính xác!");
        }

        if (!ValidationUtils.isNotEmpty(newPass)) {
            errors.put("newPassword", "Mật khẩu mới không được để trống.");
        } else if (ValidationUtils.isNotEmpty(newPass) && newPass.equals(oldPass)) {
            errors.put("newPassword", "Mật khẩu mới không được giống mật khẩu cũ!");
        } else if (!ValidationUtils.isStrongPassword(newPass)) {
            errors.put("newPassword", "Mật khẩu ≥ 8 ký tự, có chữ hoa, số và ký tự đặc biệt");
        }

        if (!ValidationUtils.isNotEmpty(confirmPass)) {
            errors.put("confirmPassword", "Vui lòng xác nhận mật khẩu mới.");
        } else if (!newPass.equals(confirmPass)) {
            errors.put("confirmPassword", "Nhập lại mật khẩu chưa chính xác");
        }

        return errors;
    }

    public Map<String, String> validateEmailUpdate(String email, String userOtp, String serverOtp, Long otpTime, dao.UserDAO userDAO, String currentEmail) {
        Map<String, String> errors = new HashMap<>();
        if (!ValidationUtils.isNotEmpty(email)) {
            errors.put("email", "Email không được để trống.");
        } else if (!ValidationUtils.isValidEmail(email)) {
            errors.put("email", "Email không đúng định dạng.");
        } else if (!email.equals(currentEmail) && userDAO.isUserEmailExists(email)) {
            errors.put("email", "Email đã tồn tại!");
        }

        if (serverOtp == null || !serverOtp.equals(userOtp)) {
            errors.put("otp", "Mã OTP không chính xác hoặc chưa gửi mã!");
        } else if (otpTime == null || (System.currentTimeMillis() - otpTime < 6000)) {
            errors.put("otp", "Mã OTP đã hết hạn (quá 60 giây)!");
        }
        return errors;
    }
}
