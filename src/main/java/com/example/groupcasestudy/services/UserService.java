package com.example.groupcasestudy.services;

import com.example.groupcasestudy.modals.dto.UserDTO;
import com.example.groupcasestudy.modals.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    String addUserToDatabase(UserDTO userDTO);

    List<UserResponse> viewAllUsersFromDatabase();

    UserResponse viewSingleUserByUserId(UUID userId);
}
