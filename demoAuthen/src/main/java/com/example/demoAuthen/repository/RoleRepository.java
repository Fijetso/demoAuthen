package com.example.demoAuthen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoAuthen.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	public Optional<Role> findByName(String name);
}
