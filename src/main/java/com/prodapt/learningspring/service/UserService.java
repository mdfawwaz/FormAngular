package com.prodapt.learningspring.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.entity.User;
import  com.prodapt.learningspring.controller.exception.BusinessException;
import  com.prodapt.learningspring.repository.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        Optional<User> optUser = userRepository.findByName(username);
        if (optUser.isEmpty()) {
        	System.out.println("no user");
           
        }
        if (!optUser.get().getPassword().equals(password)) {
            return Optional.empty();
        }
        return optUser;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

}
