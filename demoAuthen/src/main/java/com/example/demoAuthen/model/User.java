package com.example.demoAuthen.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long userId;
	private String email;
	private String password;
	private String name;
	private String lastName;
	private int active;
	@OneToMany(mappedBy="roleId", fetch = FetchType.EAGER)
	private Set<Role> roles;
	
	public User() {
		super();
	}
	public User(User user) {
		this.active = user.getActive();
		this.email = user.getEmail();
		this.lastName = user.getLastName();
		this.name = user.getName();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		this.userId = user.getUserId();
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}
