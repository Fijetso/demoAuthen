package com.example.demoAuthen.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

public class OidcUserInfo extends DefaultOidcUser {

	public OidcUserInfo(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken) {
		super(authorities, idToken);
	}
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
