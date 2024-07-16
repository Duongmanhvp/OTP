package com.example.OTP.controller;

import com.example.OTP.dto.OtpVerifitionRequest;
import com.example.OTP.dto.RegistrationRequest;
import com.example.OTP.entity.User;
import com.example.OTP.repository.UserRepository;
import com.example.OTP.service.EmailService;
import com.example.OTP.service.OtpService;
import com.example.OTP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email already registered");
		}
		
		 User user = userService.registerUser(request);
	//	String otp = otpService.generateOTP(user.getEmail());
		
		emailService.sendOtpEmail(request.getEmail(), otpService.generateOTP(request.getEmail()));
		
		return ResponseEntity.ok("OTP sent to your email. Please verify within 3 minutes.");
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifitionRequest request) {
		if (otpService.validateOTP(request.getEmail(), request.getOtp())) {
			userService.verifyUser(request.getEmail());
			return ResponseEntity.ok("Account verified successfully.");
		} else {
			return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
		}
	}
}
