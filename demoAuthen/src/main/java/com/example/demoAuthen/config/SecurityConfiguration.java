package com.example.demoAuthen.config;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService)
//		.passwordEncoder(getPasswordEncoder());
//		super.configure(auth);
//	}
//	
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
;	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("**/secured/**").hasRole("ADMIN")
		.anyRequest().permitAll()
		.and().formLogin()
//		.loginPage("/loginpage")
		.permitAll();
//		super.configure(http);
		///logout
		http.logout()
		.logoutSuccessUrl("/login")
		.logoutUrl("/logout")
		.permitAll();
	}
	

//	@Bean
//	@Override
//	protected UserDetailsService userDetailsService() {
//		UserBuilder users = User.builder().passwordEncoder(p -> "{noop}" + p);
//		UserBuilder bcryptusers = User.builder().passwordEncoder(p -> "{bcrypt}" + p);
//		
//		UserDetails fijetso = users.username("Fijetso").password("123456").roles("USER").build();
//				
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(fijetso);
//		return manager;
//	}





//	private PasswordEncoder getPasswordEncoder() {
//		PasswordEncoder passwordEncoder =
//			    PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		return passwordEncoder;
//		return new PasswordEncoder() {
//			@Override
//			public String encode(CharSequence charSequence) {
//				return charSequence.toString();
//			}
//			@Override
//			public boolean matches(CharSequence charSequence, String s) {
//				return true;
//			}
//		};
//	}
	
}
