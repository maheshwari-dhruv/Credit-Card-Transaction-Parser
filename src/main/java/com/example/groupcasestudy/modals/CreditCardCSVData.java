package com.example.groupcasestudy.modals;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "credit_card_data")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardCSVData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transactionId", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID transactionId;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String cardHolderName;

    @Column(unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String creditCardNumber;

    private LocalDateTime transactionDate;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String merchantName;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String merchantCity;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String merchantPostalCode;

    @JdbcTypeCode(SqlTypes.DOUBLE)
    private Double amount;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String currencyCode;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String transactionStatus;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String cardType;

    @OneToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CreditCardCSVData that = (CreditCardCSVData) o;
        return transactionId != null && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
