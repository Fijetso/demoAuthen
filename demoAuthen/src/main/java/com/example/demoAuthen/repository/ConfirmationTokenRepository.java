package com.example.demoAuthen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoAuthen.model.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
