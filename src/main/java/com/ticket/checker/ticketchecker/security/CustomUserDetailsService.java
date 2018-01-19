package com.ticket.checker.ticketchecker.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ticket.checker.ticketchecker.users.User;
import com.ticket.checker.ticketchecker.users.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if(!optionalUser.isPresent()) {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		
		User user = optionalUser.get();
		CustomUserDetails userDetails = new CustomUserDetails(user);
		return userDetails;
	}

}
