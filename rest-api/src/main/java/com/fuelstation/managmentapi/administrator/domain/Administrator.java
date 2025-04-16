package com.fuelstation.managmentapi.administrator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator {
    private Long id;
    // email need to be taken from credentials 
    private String email;
    private Long credentialsId;
}
