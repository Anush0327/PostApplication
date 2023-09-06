package com.prodapt.learningspring.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.controller.exception.NotLoggedInException;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Optional<User> authenticate(String username, String password) {
        Optional<User> optUser = userRepository.findByName(username);
        if (optUser.isEmpty()) {
            throw new NotLoggedInException("User not found");
        }
        if (!optUser.get().getPassword().equals(password)) {
            return Optional.empty();
        }
        return optUser;
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
    
}
