package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.exception.AppException;
import com.danish.taskmanager.mapper.UserMapper;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.danish.taskmanager.mapper.UserMapper.toDTO;


@Service
public class UserService {


    UserRepository userRepository;
    UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper=userMapper;
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
            // spring will find @ExceptionHandler annotation and will call @ExceptionHandler(AppException.class)
            throw new AppException(
                    "User Not Found",
                    "No Registered User With this Email",
                    400
            );
        }

    }

    public UserResponseDTO addUser(UserRequestDTO dto) {


        User saveUser = userRepository.save(userMapper.toEntity(dto));

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


    public UserResponseDTO registerUser(UserRequestDTO dto) {


        User saveUser = userRepository.save(userMapper.toEntity(dto));

        return toDTO(saveUser);

    }

    public  UserResponseDTO findBYEmail(String email){
        User byEmail = userRepository.findByEmail(email).get();
        return toDTO(byEmail);
    }

}
