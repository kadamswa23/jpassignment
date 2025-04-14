package com.example.jpassignment.service;

import com.example.jpassignment.dto.AddressDto;
import com.example.jpassignment.dto.ContactDto;
import com.example.jpassignment.dto.UserDto;
import com.example.jpassignment.entity.AddressEntity;
import com.example.jpassignment.entity.ContactEntity;
import com.example.jpassignment.entity.UserEntity;
import com.example.jpassignment.exception.UserNotFoundException;
import com.example.jpassignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceBeanTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceBean userServiceBean;
    @Mock
    ModelMapper modelMapper;

    private static UserEntity userEntity = null;
    private static UserDto userDto = null;

    @BeforeEach
    public void setup(){
        userEntity = getMockedUserEntity();
        userDto = getUserDto();
    }

    @Test
    void saveUser_success() {
        AddressEntity addressEntity = getAddressEntities();
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(modelMapper.map(any(UserDto.class), eq(UserEntity.class))).thenReturn(userEntity);
        when(modelMapper.map(any(AddressDto.class), eq(AddressEntity.class))).thenReturn(addressEntity);
        when(modelMapper.map(any(ContactDto.class), eq(ContactEntity.class))).thenReturn(userEntity.getContact());
        assertDoesNotThrow(() -> userServiceBean.saveUser(userDto));
    }

    @Test
    void saveUser_WithMissingEmail() {
        userDto.getContact().setEmail(null);
        assertThrows(Exception.class, () -> userServiceBean.saveUser(userDto));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.<UserEntity>of(userEntity));
        doNothing().when(userRepository).deleteById(anyLong());
        userServiceBean.deleteUser(userEntity.getUserId());
        verify(userRepository, times(1)).deleteById(userEntity.getUserId());
    }

    @Test
    public void deleteUserExpectError() {
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class , () -> {
            userServiceBean.deleteUser(userEntity.getUserId());
        });
    }
    @Test
    void WhenNotExist_thenGetUserByIdException() {
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class , () -> {
            userServiceBean.getUserById(userEntity.getUserId());
        });
    }

    @Test
    void WhenNotExist_thenGetUserByEmailException() {
        when(userServiceBean.getUserByEmail(userEntity.getContact().getEmail())).thenReturn(new ArrayList<>());
        List<UserDto> userDto = userServiceBean.getUserByEmail("non_exsting@email.com");
        assertDoesNotThrow( () -> userServiceBean.getUserByEmail(userEntity.getContact().getEmail()));
        assertTrue(userDto.isEmpty());
    }


    @Test
    void getUserByEmail_success() {
        List<UserEntity> ls = new ArrayList<>();
        ls.add(userEntity);
        when(userRepository.findByContactEmailContaining(anyString())).thenReturn(ls);
        when(modelMapper.map(any(UserEntity.class), eq(UserDto.class))).thenReturn(userDto);
        when(modelMapper.map(any(AddressEntity.class), eq(AddressDto.class))).thenReturn(new AddressDto("TEst address"));
        when(modelMapper.map(any(ContactEntity.class), eq(ContactDto.class))).thenReturn(userDto.getContact());
        List<UserDto> result = userServiceBean.getUserByEmail(userEntity.getContact().getEmail());
        assertNotNull(result);
        verify(userRepository, times(1)).findByContactEmailContaining(userEntity.getContact().getEmail());
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(any(AddressDto.class), eq(AddressEntity.class))).thenReturn(userEntity.getAddressList().get(0));
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(userEntity);
        UserDto userDto1 = userServiceBean.updateUser(1,userDto);
        assertNotNull(userDto1);
    }


    private static UserEntity getMockedUserEntity() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setFirstName("Swati");
        user.setLastName("Kadam");
        user.setDob(LocalDate.parse("2007-07-25"));

        // Setting the addresses
        List<AddressEntity> addressEntities = new ArrayList<>();
        addressEntities.add(AddressEntity.builder()
                .street("123a Main Road, Glasgow, G1 1AA")
                .build());

        addressEntities.add(AddressEntity.builder()
                .street("Marnix, Brussels, Belgium 1180")
                .build());
        user.setAddressList(addressEntities);

        // Setting the contact details
        ContactEntity contact = ContactEntity.builder()
                .homePhone("01411234567")
                .mobilePhone("0779900099987")
                .email("johndoe@email.com, johndoe@email.co.uk")
                .build();
        user.setContact(contact);
        return user;
    }

    public UserDto getUserDto(){
        ContactDto contact = ContactDto.builder()
                .email(List.of("test@Test.com"))
                .mobilePhone("324832984384")
                .homePhone("3993493899")
                .build();
        AddressDto addressDto = AddressDto.builder()
                .street("123a Main Road, Glasgow, G1 1AA")
                .build();
        UserDto user = UserDto.builder()
                .firstName("Test")
                .lastName("Lara")
                .dob(String.valueOf(LocalDate.now()))
                .build();
        user.setContact(contact);
        user.setAddresses(List.of(addressDto));

        return user;
    }
    public AddressEntity getAddressEntities(){
        return AddressEntity.builder()
                .street("Marnix, Brussels, Belgium 1180")
                .build();
    }
}