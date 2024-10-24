package com.example.app.application.port.inbound;

import com.example.app.application.dto.inbound.UpdateUserRequest;
import com.example.app.application.dto.inbound.UserRequest;
import com.example.app.domain.model.User;

import java.util.UUID;

public interface UserService {

    User createUser(UserRequest userDto);

    User getUserById(UUID id);

    User updateUser(UUID id, UpdateUserRequest updateUserDto);

    void deleteUser(UUID id);
}
