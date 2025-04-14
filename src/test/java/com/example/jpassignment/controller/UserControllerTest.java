package com.example.jpassignment.controller;

import com.example.jpassignment.dto.AddressDto;
import com.example.jpassignment.dto.ContactDto;
import com.example.jpassignment.dto.UserDto;
import com.example.jpassignment.utility.ConstantUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    private static final Long NON_EXISTING_USERID = 5000L;
    private static final Long EXISTING_USERID = 1L;
    private static UserDto validUSerDto;
    private static UserDto inValidUserDto;
    private static HttpHeaders headers;

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserController userController;
    @LocalServerPort
    private int port;



    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        validUSerDto = getUserDto();
        inValidUserDto  = getInvalidUserDto();
    }

    @Test
    @Order(1)
    void createUser_withValidUser() {
        String url = getURLWithPort(ConstantUtil.USER_GET_URL);

        ResponseEntity response = this.testRestTemplate.postForEntity(url, validUSerDto, UserDto.class );
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @Order(2)
    void expectError_withInvalidUserDetails() {
        String url = getURLWithPort(ConstantUtil.USER_GET_URL);
        assertThrows(Exception.class,() -> this.testRestTemplate.postForEntity(url, inValidUserDto , UserDto.class ));
    }

    @Test
    @Order(3)
    void getUserBy_WithExistingUserId() {
        String getApiURL = getURLWithPort(ConstantUtil.USER_GET_URL+"/"+EXISTING_USERID);
        ResponseEntity response = this.testRestTemplate.getForEntity(getApiURL, UserDto.class );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    @Order(4)
    void getUserBy_NonExistingId() {
        String getApiURL = getURLWithPort(ConstantUtil.USER_GET_URL+"/");
        ResponseEntity response = this.testRestTemplate.getForEntity(getApiURL, UserDto.class, NON_EXISTING_USERID);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }


    @Test
    @Order(5)
    void updateUser() {
        validUSerDto.getContact().setEmail(Arrays.asList("jean-mark@yahoo.com", "johndoe@email.co.uk"));
        String url = getURLWithPort(ConstantUtil.USER_GET_URL+"/");
        assertDoesNotThrow(() -> this.testRestTemplate.put(url, validUSerDto, EXISTING_USERID));

        String getApiURL = getURLWithPort(ConstantUtil.USER_GET_URL+"/"+EXISTING_USERID);
        ResponseEntity response = this.testRestTemplate.getForEntity(getApiURL, UserDto.class );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(6)
    void testDeleteUser_whenValidUserID() {
        String url = getURLWithPort(ConstantUtil.USER_GET_URL+"/");
        assertDoesNotThrow(() -> this.testRestTemplate.delete(url, EXISTING_USERID));
    }

    @Test
    @Order(6)
    void getUserByEmail() {
        String getApiURL = getURLWithPort(ConstantUtil.USER_GET_URL);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(getApiURL))
                .queryParam("emailId", "johndoe@email.co.uk");

        ResponseEntity response = this.testRestTemplate.getForEntity(builder.toUriString(), UserDto[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //assertTrue(response.toString().contains("johndoe@email.com"));
    }


    protected String getURLWithPort(String API_URL) {
        return ConstantUtil.BASE_URL + port +API_URL;
    }
    private static UserDto getUserDto() {
        UserDto user = new UserDto();
        user.setFirstName("Swati");
        user.setLastName("Kadam");
        user.setDob("2007-07-25");

        // Setting the addresses
        List<AddressDto> addressDtos = new ArrayList<>();
        addressDtos.add(AddressDto.builder()
                .street("123a Main Road, Glasgow, G1 1AA")
                .build());

        addressDtos.add(AddressDto.builder()
                .street("Marnix, Brussels, Belgium 1180")
                .build());
        user.setAddresses(addressDtos);

        // Setting the contact details
        ContactDto contact = ContactDto.builder()
                .homePhone("01411234567")
                .mobilePhone("0779900099987")
                .email(Arrays.asList("johndoe@email.com", "johndoe@email.co.uk"))
                .build();
        user.setContact(contact);
        return user;
    }

    private static UserDto getInvalidUserDto() {
        UserDto user = new UserDto();
        user.setFirstName("Swati");
        user.setLastName("Kadam");
        user.setDob("2007-07-25");

        // Setting the addresses
        List<AddressDto> addressDtos = new ArrayList<>();
        addressDtos.add(AddressDto.builder()
                .street("123a Main Road, Glasgow, G1 1AA")
                .build());

        addressDtos.add(AddressDto.builder()
                .street("Marnix, Brussels, Belgium 1180")
                .build());
        user.setAddresses(addressDtos);

        // Setting the contact details
        ContactDto contact = ContactDto.builder()
                .homePhone("01411234567")
                .mobilePhone("0779900099987")
                .build();
        user.setContact(contact);
        return user;
    }
}