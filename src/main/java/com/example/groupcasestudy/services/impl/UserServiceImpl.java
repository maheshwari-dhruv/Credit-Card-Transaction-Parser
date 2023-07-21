package com.example.groupcasestudy.services.impl;

import com.example.groupcasestudy.convertor.UserConvertor;
import com.example.groupcasestudy.modals.User;
import com.example.groupcasestudy.modals.dto.UserDTO;
import com.example.groupcasestudy.modals.responses.UserResponse;
import com.example.groupcasestudy.repositories.UserRepository;
import com.example.groupcasestudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvertor convertor;

    @Override
    public String addUserToDatabase(UserDTO userDTO) {
        User user = convertor.convertUserDTOtoUser(userDTO);
        userRepository.save(user);
        return "User Saved Successfully";
    }

    @Override
    public List<UserResponse> viewAllUsersFromDatabase() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User user: allUsers) {
            userResponses.add(convertor.convertUserToUserResponse(user));
        }

        return userResponses;
    }

    @Override
    public UserResponse viewSingleUserByUserId(UUID userId) {
        User user = userRepository.findUserByUserId(userId);
        return convertor.convertUserToUserResponse(user);
    }
}
