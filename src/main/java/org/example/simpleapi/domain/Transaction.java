package org.example.simpleapi.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
@Builder
@Getter
public class Transaction {
    private Integer transactionId;
    private Integer customerId ;
    private BigDecimal tranAmount;
    private OffsetDateTime tranWhen;
}
