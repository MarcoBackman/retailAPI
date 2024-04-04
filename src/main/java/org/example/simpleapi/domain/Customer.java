package org.example.simpleapi.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Customer {
    private Integer customerId;
    private String userName;
    private String firstName;
    private String lastName;
}
