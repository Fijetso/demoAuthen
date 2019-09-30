package com.example.demoAuthen.config;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demoAuthen.model.OAuth2UserInfo;
import com.example.demoAuthen.model.OidcUserInfo;
import com.example.demoAuthen.model.Role;
import com.example.demoAuthen.model.User;
import com.example.demoAuthen.repository.RoleRepository;
import com.example.demoAuthen.repository.UserRepository;
import com.example.demoAuthen.service.CustomUserDetailsService;

//// khong hieu
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/signup", "/login", "/logout").permitAll();

		// Chỉ cho phép user có quyền ADMIN truy cập đường dẫn /admin/**
		http.authorizeRequests().antMatchers("*/admin/**").access("hasRole('ROLE_ADMIN')");
		// Chỉ cho phép user có quyền ADMIN hoặc USER truy cập đường dẫn /user/**
		http.authorizeRequests().antMatchers("*/user/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')");
		// Khi người dùng đã login, với vai trò USER, Nhưng truy cập vào trang yêu cầu
		// vai trò ADMIN, sẽ chuyển hướng tới trang /403
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().anyRequest().authenticated().and().oauth2Login().userInfoEndpoint()
				.userService(this.oauth2UserService()).oidcUserService(this.oidcUserService());
	}

	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		return (OAuth2UserRequest userRequest) -> {
			OAuth2User oauth2User = delegate.loadUser(userRequest);
			String email = (String) oauth2User.getAttributes().get("email");
			Optional<User> optionalUser = userRepository.findByEmail(email);
			Set<GrantedAuthority> authorities = new HashSet<>();
			optionalUser.ifPresent(user -> {
				user.getRoles().forEach(authority -> {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getName()));
				});
			});
			OAuth2UserInfo oauth2UserInfo = new OAuth2UserInfo(authorities, oauth2User.getAttributes(), "id");
			oauth2UserInfo.setUsername(optionalUser.get().getName()); /// getUserName()
			return oauth2User;
		};
	}

	private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();
		return (OidcUserRequest userRequest) -> {
			OidcUser oidcUser = delegate.loadUser(userRequest);
			String email = (String) oidcUser.getEmail();
			Optional<User> optionalUser = userRepository.findByEmail(email);
			Set<GrantedAuthority> authorities = new HashSet<>();
			if (optionalUser.isPresent()) {
				optionalUser.get().getRoles().forEach(authority -> {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getName()));
				});
			} else {
				authorities.addAll(oidcUser.getAuthorities());
				Set<Role> roles = new HashSet<>();
				roles.add(roleRepository.findByName("USER").get());
				User newUser = new User(oidcUser.getFullName(), oidcUser.getFamilyName(), oidcUser.getEmail(), roles);
				userRepository.save(newUser);
			}
//			optionalUser.ifPresentOrElse(user ->{
//				user.getRoles().forEach(
//					authority ->{
//						authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getName()));
//						});
//					},
//					()-> {
//						authorities.addAll(oidcUser.getAuthorities());
//						Set<Role> roles = new HashSet<>();
//						roles.add(roleRepository.findByName("USER").get());
//						User newUser = new User(oidcUser.getFullName(),oidcUser.getFamilyName(),oidcUser.getEmail(), roles);
//						userRepository.save(newUser);
//						});
			/// lỗi chỗ này, vs k lưu vô role_user
			OidcUserInfo oidcUserInfo = new OidcUserInfo(authorities, oidcUser.getIdToken());
			oidcUserInfo.setUsername(oidcUser.getFullName()); /// getUserName()
			return oidcUserInfo;
		};
	}

}
