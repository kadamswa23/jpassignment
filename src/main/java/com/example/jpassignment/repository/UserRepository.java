package com.example.jpassignment.repository;

import com.example.jpassignment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

   public List<UserEntity> findByContactEmailContaining(String email);

}
