package com.compass.ecommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.compass.ecommerce.models.User;

public interface UserRepository extends JpaRepository<User, String> {
	UserDetails findByLogin(String login);
}
