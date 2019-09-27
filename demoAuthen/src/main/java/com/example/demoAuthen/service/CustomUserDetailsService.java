package com.example.demoAuthen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demoAuthen.model.User;
import com.example.demoAuthen.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByName(username);
		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//		optionalUser
//		.ifPresent(user ->  new CustomUserDetails(user));
		///khong hieu khuc nay
		User user = optionalUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(authority ->
			authorities.add(new SimpleGrantedAuthority("ROLE_"+authority.getName()))
				);
		return org.springframework.security.core.userdetails.User.builder().username(user.getName()).password(user.getPassword()).authorities(authorities).build();
	}
}
