package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.service.MessageService;
import com.example.service.AccountService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@Controller
public class SocialMediaController {

    private AccountService accountService;

    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.register(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); 
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) throws AuthenticationException {
        try {
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loggedInAccount);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message savedMessage = messageService.createNewMessage(newMessage);
            return ResponseEntity.ok(savedMessage); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); 
        }

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        try {
            Message message = messageService.getMessageById(messageId);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok().body("");
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId) {
        int result = messageService.deleteMessage(messageId);
        return result == 1 ? ResponseEntity.ok(1) : ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessageText(@PathVariable int messageId,
            @RequestBody Map<String, String> request) {
        try {
            String newText = request.get("messageText");
            int result = messageService.updateMessageText(messageId, newText);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().build(); 
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessageByUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessageByUser(accountId);
        return ResponseEntity.ok(messages);
    }

}
