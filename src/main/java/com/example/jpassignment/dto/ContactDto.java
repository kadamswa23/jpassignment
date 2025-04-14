package com.example.jpassignment.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDto {

    @NotNull
    @NotEmpty(message = "Email field is required")
    private List<String> email;

    @Digits(integer = 10, fraction = 0)
    private String homePhone;
    private String mobilePhone;

}
