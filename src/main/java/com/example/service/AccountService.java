package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account newAccount) {
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();
        if (username == null || username == "")
            throw new IllegalArgumentException("Username is required.");
        if (password.length() < 4)
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        if (accountRepository.findByUsername(username) != null)
            throw new DuplicateUsernameException();
        return accountRepository.save(newAccount);
    }

    public Account login(String username, String password) {
        if (username == null || password == null)
            return null;
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public static class DuplicateUsernameException extends RuntimeException {}
}
