package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User findUser(int userId) {
        Optional<User> userbyId = userRepository.findById(userId);
        User user = null;
        if (userbyId.isPresent()) {
            user = userbyId.get();
        } else {
            // exception handling
        }

        return user;
    }

    public User addUser(UserRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(User.Role.valueOf(dto.getRole().toUpperCase()));

        return userRepository.save(user);


    }

}
