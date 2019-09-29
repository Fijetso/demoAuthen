package com.example.demoAuthen.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoAuthen.model.OAuth2UserInfo;
import com.example.demoAuthen.model.OidcUserInfo;

@RequestMapping("/rest/hello")
@RestController
public class controller {
	@GetMapping("/all")
	public String hello() {
		return "Hello USER";
	}
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/secured/all")
	public String securedHello() {
		return "Hello ADMIN";
	}
	private String getUserName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "Anonymous";
		if(principal instanceof OidcUserInfo) {
			username = ((OidcUserInfo)principal).getUsername();
		}else if(principal instanceof OAuth2UserInfo) {
			username = ((OAuth2UserInfo)principal).getUsername();
		}
		return username;
	}
}
