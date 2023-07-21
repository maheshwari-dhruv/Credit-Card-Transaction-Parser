package com.example.groupcasestudy.modals;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "user")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userId", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID userId;

    @Column(nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String username;

    @Column(nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String email;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany
    @ToString.Exclude
    private List<CreditCardCSVData> creditCardCSVData;
}
