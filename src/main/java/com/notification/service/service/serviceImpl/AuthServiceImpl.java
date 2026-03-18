package com.notification.service.service.serviceImpl;

import com.notification.service.constants.AppConstants;
import com.notification.service.enums.OtpType;
import com.notification.service.exception.UserNotFoundException;
import com.notification.service.model.Otp;
import com.notification.service.model.User;
import com.notification.service.repository.OtpRepository;
import com.notification.service.repository.UserRepository;
import com.notification.service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String sendEmail(String toEmail,String otp) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(toEmail);
            message.setSubject("Email Verification - OTP");

            message.setText(
                    "Hello,\n\n" +
                            "Your OTP for account verification is: " + otp + "\n\n" +
                            "This OTP is valid for 5 minutes.\n\n" +
                            "Do not share this OTP with anyone.\n\n" +
                            "Thank you."
            );

            mailSender.send(message);
            return "OTP sent successfully";

        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    @Override
    public String register(String email, String password) {
        email = email.trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already registered";
        }

        String oneTimeCode = generateOtp();

        Otp otp = otpRepository
                .findByEmailAndType(email, OtpType.REGISTER)
                .orElse(new Otp());

        otp.setEmail(email);
        otp.setPassword(passwordEncoder.encode(password));
        otp.setOtp(passwordEncoder.encode(oneTimeCode));
        otp.setType(OtpType.REGISTER);
        otp.setOtpExpiry(LocalDateTime.now()
                .plusMinutes(AppConstants.OTP_EXPIRY_MINUTES));
        otp.setCreatedAt(LocalDateTime.now());

        otpRepository.save(otp);
        return sendEmail(email, oneTimeCode);
    }

    @Override
    public String verifyOtp(String email, String otp) {
        email = email.trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            return "User already verified";
        }
        Otp opt = otpRepository.findTopByEmailAndType(email,OtpType.REGISTER)
                .orElseThrow(() -> new UserNotFoundException("Uer not found or OTP expired"));

        if (!passwordEncoder.matches(otp, opt.getOtp())) {
            return "Invalid OTP";
        }
        Duration duration = Duration.between(opt.getOtpExpiry(), LocalDateTime.now());
        if (duration.toMinutes() > AppConstants.OTP_EXPIRY_MINUTES) {
            return "OTP expired";
        }

        User user = new User();
        user.setEmail(opt.getEmail());
        user.setPassword(opt.getPassword());

        userRepository.save(user);
        otpRepository.delete(opt);

        return "Account created successfully";
    }

}
