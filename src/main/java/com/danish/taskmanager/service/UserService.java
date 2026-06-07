package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.*;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.exception.AppException;
import com.danish.taskmanager.mapper.UserMapper;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.specification.TaskSpecification;
import com.danish.taskmanager.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


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
            userDto.add(userMapper.toDTO(user));
        }

        return userDto;
    }
    public Page<UserResponseDTO> getFilteredUser(UserFilter filter, Pageable pageable) {
        Specification<User> spec = UserSpecification.withFilters(filter);
        return userRepository.findAll(spec, pageable)
                .map(userMapper::toDTO);
    }


    public UserResponseDTO findUser(Long userID) {
        Optional<User> userByID = userRepository.findById(userID);

        if (userByID.isPresent()) {
            return userMapper.toDTO(userByID.get());
        } else {
            // spring will find @ExceptionHandler annotation and will call @ExceptionHandler(AppException.class)
            throw new AppException(
                    "User Not Found",
                    "No Registered User With this Email",
                    400
            );
        }

    }
    public UserRequestDTO findUserForForm(Long userID) {
        Optional<User> userByID = userRepository.findById(userID);

        if (userByID.isPresent()) {
            User user = userByID.get();
            UserRequestDTO dto = new UserRequestDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().toString());
            // password left blank intentionally
            return dto;
        } else {
            throw new AppException("User Not Found", "No Registered User With this ID", 400);
        }
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {


        User saveUser = userRepository.save(userMapper.toEntity(dto));

        return userMapper.toDTO(saveUser);

    }

    public User deleteUser(Long userID) {
        Optional<User> userByID = userRepository.findById(userID);
        if (userByID.isPresent()) {
            userRepository.delete(userByID.get());
            return userByID.get();
        } else {
            throw new NoSuchElementException("User with ID " + userID + " not found");
        }
    }

    public UserResponseDTO updateUser(Long userID, UserRequestDTO userRequestDTO) {

        User userByID = userRepository.findById(userID).orElseThrow(() -> new NoSuchElementException("User with ID " + userID + " not found"));
        userByID.setName(userRequestDTO.getName());
        userByID.setEmail(userRequestDTO.getEmail());
        userByID.setRole(User.Role.valueOf(userRequestDTO.getRole()));
        /* call save(userByID): JPA sees ID is not null + entity exists So UPDATE is called, not INSERT
         */
        userRepository.save(userByID);

        return userMapper.toDTO(userByID);


    }


    public UserResponseDTO registerUser(UserRequestDTO dto) {


        User saveUser = userRepository.save(userMapper.toEntity(dto));

        return userMapper.toDTO(saveUser);

    }

    public  UserResponseDTO findBYEmail(String email){
        User byEmail = userRepository.findByEmail(email).orElseThrow(() -> new AppException("User not found", "USER_NOT_FOUND", 404));
        return userMapper.toDTO(byEmail);
    }

}
