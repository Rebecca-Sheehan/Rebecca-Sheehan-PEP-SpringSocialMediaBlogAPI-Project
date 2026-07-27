package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount) {
        try {
            Account responseAccount = accountService.register(newAccount);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseAccount);
        } catch (AccountService.DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .build();
        }
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account responseAccount = accountService.login(account.getUsername(), account.getPassword());
        if (responseAccount == null) 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseAccount);
    } 

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message responseMessage = messageService.createMessage(newMessage);
            return ResponseEntity.status(200)
                    .body(responseMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .build();
        }
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> responseList = messageService.getAllMessages();
        return ResponseEntity.status(200)
                .body(responseList);
    }
    
    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") Integer messageId) {
        Message responseMessage = messageService.getMessageById(messageId);
        return ResponseEntity.status(200)
                .body(responseMessage);
    }
    
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable("messageId") Integer messageId) {
        int rowsDeleted = messageService.deleteMessage(messageId);
        if (rowsDeleted == 0)
            return ResponseEntity.status(200)
                    .build();
        return ResponseEntity.status(200)
                .body(rowsDeleted);
    }
    
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("messageId") Integer messageId, @RequestBody Map<String, String> newMessage) {
        String newMessageText = newMessage.get("messageText");
        int rowsUpdated = messageService.updateMessage(messageId, newMessageText);
        if (rowsUpdated == 0)
            return ResponseEntity.status(400)
                    .build();
        return ResponseEntity.status(200)
                .body(rowsUpdated);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByPostedBy(@PathVariable("accountId") Integer accountId) {
        List<Message> responseList = messageService.getMessagesByPostedBy(accountId);
        return ResponseEntity.status(200)
                .body(responseList);
    }
}
