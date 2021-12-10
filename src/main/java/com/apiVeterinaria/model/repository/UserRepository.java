package com.apiVeterinaria.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiVeterinaria.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	//save
	//findAll
	//findById
	//delete
	boolean existsByUsername(String username);
	User findByUsername(String username);

}
