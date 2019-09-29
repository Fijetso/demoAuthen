package com.example.demoAuthen.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class OAuth2UserInfo extends DefaultOAuth2User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuth2UserInfo(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
			String nameAttributeKey) {
		super(authorities, attributes, nameAttributeKey);
	}
	private String username;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
