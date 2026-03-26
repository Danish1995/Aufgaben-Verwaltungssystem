package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.danish.taskmanager.mapper.UserMapper.toDTO;
import static com.danish.taskmanager.mapper.UserMapper.toEntity;

@Service
public class UserService {


    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> findAll() {
        List<User> all = userRepository.findAll();
        List<UserResponseDTO> userDto = new ArrayList<>();

        for (User user : all) {
            userDto.add(toDTO(user));
        }

        return userDto;
    }


    public UserResponseDTO findUser(int userID) {
        Optional<User> userByID = userRepository.findById(userID);

        if (userByID.isPresent()) {
            return toDTO(userByID.get());
        } else {
            throw new NoSuchElementException("User with ID " + userID + " not found");
        }

    }

    public UserResponseDTO addUser(UserRequestDTO dto) {

        User saveUser = userRepository.save(toEntity(dto));

        return toDTO(saveUser);

    }

    public User deleteUser(int userID) {
        Optional<User> userByID = userRepository.findById(userID);
        if (userByID.isPresent()) {
            userRepository.delete(userByID.get());
            return userByID.get();
        } else {
            throw new NoSuchElementException("User with ID " + userID + " not found");
        }
    }

    public UserResponseDTO updateUser(Integer userID, UserRequestDTO userRequestDTO) {

        User userByID = userRepository.findById(userID).orElseThrow(() -> new NoSuchElementException("User with ID " + userID + " not found"));
        userByID.setName(userRequestDTO.getName());
        userByID.setEmail(userRequestDTO.getEmail());
        userByID.setRole(User.Role.valueOf(userRequestDTO.getRole()));
        /* call save(userByID): JPA sees ID is not null + entity exists So UPDATE is called, not INSERT
         */
        userRepository.save(userByID);

        return toDTO(userByID);


    }
}
