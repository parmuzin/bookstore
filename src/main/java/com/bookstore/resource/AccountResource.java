package com.bookstore.resource;

import com.bookstore.model.User;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountResource {

    private UserService userService;

    @Autowired
    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /register : register the user.
     *
     * @param user the user to register
     * @return the ResponseEntity with status 201 (Created) and with body the message,
     * or with status 400 (Bad Request) if the username already exists
     */
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (userService.findByUserName(user.getUserName()) != null) {
            return new ResponseEntity<>("usernameExists", HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(user);
            return new ResponseEntity<>("User created successfully",HttpStatus.CREATED);
        }
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     */
    @GetMapping("/account")
    public User getAccount(Principal principal) {
        return userService.findByUserName(principal.getName());
   }
}
