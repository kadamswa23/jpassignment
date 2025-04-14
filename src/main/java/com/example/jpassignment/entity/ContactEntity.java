package com.example.jpassignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="contact")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contactId")
    private Long contactId;

    @Column(name="email" , nullable = false)
    private String email;
    @Column(name="home_phone")
    private String homePhone;
    @Column(name="mobile_phone")
    private String mobilePhone;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Override
    public boolean equals(Object o){
        if(this == o ) return true;
        if( o == null || getClass() != o.getClass()) return  false;
        ContactEntity contact = (ContactEntity) o;
        return homePhone.equals(contact.getHomePhone()) &&
                mobilePhone.equals(contact.getMobilePhone());
    }

    @Override
    public int hashCode(){
        return Objects.hash(homePhone, mobilePhone);
    }

}
