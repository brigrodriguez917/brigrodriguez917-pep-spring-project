package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }
        return accountRepository.save(account);

    }

    public Account login(String username, String password) throws AuthenticationException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!account.getPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password");
        }
        return account;
    }
}
