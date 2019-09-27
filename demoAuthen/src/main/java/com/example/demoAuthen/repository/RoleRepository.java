package com.example.demoAuthen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoAuthen.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
