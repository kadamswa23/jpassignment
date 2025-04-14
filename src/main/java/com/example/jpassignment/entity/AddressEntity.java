package com.example.jpassignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Builder
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="userId")
    private UserEntity user;

    @Override
    public boolean equals(Object o){
        if(this == o ) return true;
        if( o == null || getClass() != o.getClass()) return  false;
        AddressEntity address = (AddressEntity) o;
        return street.equals(address.getStreet());
    }

    @Override
    public int hashCode(){
        return Objects.hash(street);
    }

}
