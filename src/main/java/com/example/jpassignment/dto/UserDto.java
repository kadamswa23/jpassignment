package com.example.jpassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    public Long userId;
    public String firstName;
    private String lastName;
    private String dob;
    private List<AddressDto> addresses;
    private ContactDto contact;
}
