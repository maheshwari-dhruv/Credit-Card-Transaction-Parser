package com.example.groupcasestudy.controllers;

import com.example.groupcasestudy.modals.dto.UserDTO;
import com.example.groupcasestudy.modals.responses.UserResponse;
import com.example.groupcasestudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserControler {

    @Autowired
    private UserService userService;

    // read all
    @GetMapping("/view-all")
    public List<UserResponse> viewAll() {
        return userService.viewAllUsersFromDatabase();
    }

    // read single
    @GetMapping("/view-single/{userId}")
    public UserResponse viewSingleUser(@RequestParam("userId") UUID userId) {
        return userService.viewSingleUserByUserId(userId);
    }

    // create
    @PostMapping("/add-user")
    public String addUser(@RequestBody UserDTO userDTO) {
        return userService.addUserToDatabase(userDTO);
    }
    // update
    // delete
}
