package com.bookstore.service.impl;

import com.bookstore.model.Authority;
import com.bookstore.model.User;
import com.bookstore.repository.AuthorityRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.SecurityConstants;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthorityRepository authorityRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User createUser(User user) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne(SecurityConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getUserPassword());
        newUser.setUserName(user.getUserName());
        newUser.setUserPassword(encryptedPassword);
        newUser.setUserFirstName(user.getUserFirstName());
        newUser.setUserLastName(user.getUserLastName());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.delete(userId);
    }
}
