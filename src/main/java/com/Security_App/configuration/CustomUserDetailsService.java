package com.Security_App.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.Security_App.entity.User;
import com.Security_App.service.UserService;
import com.Security_App.utils.Constants.UserStatus;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = this.userService.getUserByEmailAndStatus(email, UserStatus.ACTIVE.value());
		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		// Assuming the user entity has a field for OTP
		customUserDetails.setOtp(user.getOtp()); // Set OTP in CustomUserDetails

		return customUserDetails;
	}

}
