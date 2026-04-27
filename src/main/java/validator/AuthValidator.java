    package validator;

    import dao.UserDAO;
    import java.util.HashMap;
    import java.util.Map;
    import util.ValidationUtils;

    public class AuthValidator {

        private UserDAO userDAO;

        public AuthValidator(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        public Map<String, String> validateRegister(String username, String fullName, String email,
                                                    String password, String confirmPassword,
                                                    String userOtp, String serverOtp, Long otpTime) {

            Map<String, String> errors = new HashMap<>();

            // Validate Username
            if (!ValidationUtils.isNotEmpty(username)) {
                errors.put("username", "Tên người dùng không được để trống.");
            } else if (!ValidationUtils.isAlphanumeric(username)) {
                errors.put("username", "Tên người dùng không được chứa kí tự đặc biệt và khoảng trống");
            } else if (userDAO.isUserNameExists(username)) {
                errors.put("username", "Tên người dùng đã tồn tại!");
            }

            // Validate Fullname
            if (ValidationUtils.isNotEmpty(fullName) && !ValidationUtils.isLetterAndSpace(fullName)) {
                errors.put("fullname", "Họ và tên không được chứa kí tự đặc biệt");
            }

            // Validate Email
            if (!ValidationUtils.isNotEmpty(email)) {
                errors.put("email", "Email không được để trống.");
            } else if (!ValidationUtils.isValidEmail(email)) {
                errors.put("email", "Email không đúng định dạng.");
            } else if (userDAO.isUserEmailExists(email)) {
                errors.put("email", "Email đã tồn tại!");
            }

            // Validate Password
            if (!ValidationUtils.isNotEmpty(password)) {
                errors.put("password", "Mật khẩu không được để trống");
            } else if (!ValidationUtils.isStrongPassword(password)) {
                errors.put("password", "Mật khẩu ≥ 8 ký tự, có chữ hoa, số và ký tự đặc biệt");
            } else if (!password.equals(confirmPassword)) {
                errors.put("confirmPassword", "Nhập lại mật khẩu chưa chính xác");
            }

            // Validate OTP
            if (serverOtp == null || !serverOtp.equals(userOtp)) {
                errors.put("otp", "Mã OTP không chính xác!");
            } else if (otpTime == null || (System.currentTimeMillis() - otpTime > 60000)) {
                errors.put("otp", "Mã OTP đã hết hạn (quá 60 giây)!");
            }

            return errors;
        }
    }
