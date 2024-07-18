package com.compass.ecommerce.dtos;

import com.compass.ecommerce.models.UserRole;

public record RegisterDto(String login, String password, UserRole role) {

}
