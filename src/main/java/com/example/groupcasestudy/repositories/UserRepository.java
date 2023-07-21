package com.example.groupcasestudy.repositories;

import com.example.groupcasestudy.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUserId(UUID userId);
}
