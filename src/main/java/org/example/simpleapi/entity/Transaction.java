package org.example.simpleapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TRANSACTION")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "TRAN_WHEN")
    private OffsetDateTime tranWhen;
    @Column(name = "TRAN_AMOUNT")
    private BigDecimal tranAmount;
}
