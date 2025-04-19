
package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController
{
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;
 
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account)
    {
        Optional<Account> created = accountService.register(account);
        if(created.isEmpty())
        {
            if(account.getUsername() != null && accountService.login(account).isPresent())
            {
                return ResponseEntity.status(409).build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(created.get());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account)
    {
        Optional<Account> found = accountService.login(account);
        return found.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message)
    {
        Optional<Message> created = messageService.createMessage(message);
        return created.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages()
    {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessage(@PathVariable int id)
    {
        return messageService.getMessageById(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.ok().body(null));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int id)
    {
        int result = messageService.deleteMessage(id);
        return result == 1 ? ResponseEntity.ok(result) : ResponseEntity.ok().body(null);
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Map<String, String> body)
    {
        String newText = body.get("messageText");
        int updated = messageService.updateMessage(id, newText);
        return updated == 1 ? ResponseEntity.ok(updated) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable int accountId)
    {
        return messageService.getMessagesByUser(accountId);
    }
}
