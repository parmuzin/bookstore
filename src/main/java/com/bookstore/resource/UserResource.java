package com.bookstore.resource;

import com.bookstore.model.Order;
import com.bookstore.model.User;
import com.bookstore.security.SecurityConstants;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing User.
 */
@RestController
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /users : Create a new user.
     *
     * @param user the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the message,
     * or with status 400 (Bad Request) if the user has already an ID
     */
    @PostMapping("/users")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (user.getUserId() != null) {
            return new ResponseEntity<>("idexists", HttpStatus.BAD_REQUEST);
        } else if (userService.findByUserName(user.getUserName()) != null) {
            return new ResponseEntity<>("usernameExists", HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(user);
            return new ResponseEntity<>("User created successfully",HttpStatus.CREATED);
        }
    }

    /**
     * PUT /users : Updates an existing user.
     *
     * @param user the user to update
     * @return the ResponseEntity with state 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid
     */
//    @RequestMapping(value="users/{id}", method = RequestMethod.PUT)
    @PutMapping("/users")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (user.getUserId() == null) {
            return createUser(user);
        }
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /users : get all the users.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users in body,
     * or with status 204 (NO CONTENT) if there no users in repository
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * GET /users/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user,
     * or with status 404 (NOT FOUND)
     */
    @GetMapping("/users/{id:\\d+}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * DELETE /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{id:\\d+}")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity<>("userNotFound", HttpStatus.NOT_FOUND);
        }

        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = userService.findByUserName(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}