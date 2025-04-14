package com.example.jpassignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;
    private String lastName;
    private LocalDate dob;

    @OneToMany(mappedBy ="user" , fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<AddressEntity> addressList;

    @OneToOne(mappedBy ="user" ,fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    ContactEntity contact;


    @Override
    public boolean equals(Object o){
        if(this == o ) return true;
        if( o == null || getClass() != o.getClass()) return  false;
        UserEntity user = (UserEntity) o;
        return firstName.equals(user.getFirstName()) &&
                lastName.equals(user.getLastName()) &&
                dob.isEqual(user.getDob()) &&
                contact.equals(user.getContact());
    }

    @Override
    public int hashCode(){
        return Objects.hash(firstName, lastName, dob, contact);
    }

}
