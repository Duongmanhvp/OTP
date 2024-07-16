package com.example.OTP.service;

import com.example.OTP.dto.RegistrationRequest;
import com.example.OTP.entity.User;
import com.example.OTP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	
	public User registerUser(RegistrationRequest req) {
		User user = new User();
		user.setEmail(req.getEmail());
		user.setPassword(req.getPassword());
		user.setVerified(false);
		return userRepository.save(user);
	}
	
	public void verifyUser(String email) {
		userRepository.findByEmail(email).ifPresent(user -> {
			user.setVerified(true);
			userRepository.save(user);
		});
	}
}
