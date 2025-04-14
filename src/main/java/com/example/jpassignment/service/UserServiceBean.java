package com.example.jpassignment.service;

import com.example.jpassignment.dto.AddressDto;
import com.example.jpassignment.dto.ContactDto;
import com.example.jpassignment.dto.UserDto;
import com.example.jpassignment.entity.AddressEntity;
import com.example.jpassignment.entity.ContactEntity;
import com.example.jpassignment.entity.UserEntity;
import com.example.jpassignment.exception.UserNotFoundException;
import com.example.jpassignment.repository.AddressRepository;
import com.example.jpassignment.repository.ContactRepository;
import com.example.jpassignment.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceBean implements UserService {

    Logger log =  LoggerFactory.getLogger(UserServiceBean.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<UserDto> getUserData(int offset, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(sortBy));
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        List<UserEntity> usersList = userPage.getContent();
        return usersList.stream()
                .map(this::modelToDto   ).collect(Collectors.toList());
    }

    private UserDto modelToDto(UserEntity userEntity) {
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            List<AddressDto> addressDtos = new ArrayList<>();
             for (AddressEntity addressEntity : userEntity.getAddressList()){
                AddressDto addressDto = modelMapper.map(addressEntity, AddressDto.class);
                 addressDtos.add(addressDto);
            }
            ContactDto contactDto = modelMapper.map(userEntity.getContact(), ContactDto.class);
        String emailIds = userEntity.getContact().getEmail();
        contactDto.setEmail(List.of(emailIds.split(",")));
        userDto.setAddresses(addressDtos);
            userDto.setContact(contactDto);
            return userDto;
    }

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {

            if(Objects.isNull(userDto.getContact()) || Objects.isNull(userDto.getContact().getEmail()) ||
                    userDto.getContact().getEmail().isEmpty()){
                log.error("Email is required field ");
                throw new IllegalArgumentException("Email field is required");
            }
            List<AddressEntity> addressEntities = new ArrayList<>();
            UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
            //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            userEntity.setDob(LocalDate.parse(userDto.getDob(), formatter));
            for(AddressDto addressDto : userDto.getAddresses()){
                AddressEntity addressEntity =  modelMapper.map(addressDto, AddressEntity.class);
                addressEntity.setUser(userEntity);
                addressEntities.add(addressEntity);
            }
            ContactEntity contactEntity = modelMapper.map(userDto.getContact(), ContactEntity.class);
            contactEntity.setUser(userEntity);

            userEntity.setAddressList(addressEntities);
            userEntity.setContact(contactEntity);
            UserEntity user = userRepository.save(userEntity);
            log.info("User profile saved successfully");
    }

    @Override
    public void deleteUser(Long userId) {
       UserEntity userEntity = userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException("User not found with ID "+userId) );
       userRepository.deleteById(userId);
        log.info("User profile deleted successfully");
    }

    @Override
    public UserDto getUserById(long userId) {
        log.debug("Fetch user profile with ID"+userId);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException("User not found with ID "+userId));
        return modelToDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        log.debug("Update user profile with ID"+userId);
        List<AddressEntity> addressEntities = new ArrayList<>();

        UserEntity userEntity = userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException("User not found with ID "+userId) );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        userEntity.setDob(LocalDate.parse(userDto.getDob(), formatter));
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        userEntity.getAddressList().clear();
        // create new address entries
        for(AddressDto addressDto : userDto.getAddresses()){
            AddressEntity addressEntity =  modelMapper.map(addressDto, AddressEntity.class);
            addressEntity.setUser(userEntity);
            addressEntities.add(addressEntity);
        }
        userEntity.getAddressList().addAll(addressEntities);

        // update existing contact
        ContactEntity contactEntity = userEntity.getContact();
        contactEntity.setEmail(userDto.getContact().getEmail().stream().collect(Collectors.joining(",")));
        contactEntity.setHomePhone(userDto.getContact().getHomePhone());
        contactEntity.setMobilePhone(userDto.getContact().getMobilePhone());
        contactEntity.setUser(userEntity);

        userEntity.setContact(contactEntity);
        UserEntity user = userRepository.saveAndFlush(userEntity);
        userDto.setUserId(userId);
        log.debug("User profile with ID "+ userId +" updated successfully");
        return userDto;
    }

    @Override
    public List<UserDto> getUserByEmail(String email) {
        log.info("Fetch user profile with email "+ email );
        List<UserEntity> byContactEmailContaining = userRepository.findByContactEmailContaining(email);
        List<UserDto>  userDtoList =  new ArrayList<>();
        for(UserEntity userEntity : byContactEmailContaining){
            userDtoList.add(modelToDto(userEntity));
        }
        return userDtoList;
    }
}
