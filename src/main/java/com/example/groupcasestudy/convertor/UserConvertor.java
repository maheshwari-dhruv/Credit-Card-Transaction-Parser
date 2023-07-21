package com.example.groupcasestudy.convertor;

import com.example.groupcasestudy.modals.User;
import com.example.groupcasestudy.modals.dto.UserDTO;
import com.example.groupcasestudy.modals.responses.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class UserConvertor {

    public User convertUserDTOtoUser(UserDTO userDTO) {
        return User.builder()
                .userId(UUID.randomUUID())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .creditCardCSVData(new ArrayList<>())
                .build();
    }

    public UserResponse convertUserToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .creditCardCSVData(user.getCreditCardCSVData())
                .build();
    }
}
