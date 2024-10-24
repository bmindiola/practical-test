package com.example.app.domain.service;

import com.example.app.application.dto.inbound.UpdateUserRequest;
import com.example.app.application.dto.inbound.UserRequest;
import com.example.app.application.port.inbound.UserService;
import com.example.app.application.port.outbound.UserRepository;
import com.example.app.domain.exception.ConflictException;
import com.example.app.domain.exception.ResourceNotFoundException;
import com.example.app.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRequest userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new ConflictException("User already exists with email: " + userDto.getEmail());

        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new ConflictException("User already exists with username: " + userDto.getUsername());

        User newUser = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User updateUser(UUID id, UpdateUserRequest userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User not found with id: " + id);

        userRepository.deleteById(id);
    }
}
