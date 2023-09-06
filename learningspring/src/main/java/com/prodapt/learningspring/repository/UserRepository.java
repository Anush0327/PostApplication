package com.prodapt.learningspring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends CrudRepository<User, Long>{
    
    @Transactional
    public Optional<User> findByName(String name);
}
