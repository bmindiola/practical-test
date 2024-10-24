package com.example.app.domain.service;

import com.example.app.application.dto.inbound.LoginRequest;
import com.example.app.application.dto.outbound.LoginResponse;
import com.example.app.application.port.inbound.AuthService;
import com.example.app.application.port.outbound.UserRepository;
import com.example.app.domain.model.User;
import com.example.app.infrastructure.adapter.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User authenticatedUser = userRepository.findByUsername(username).orElseThrow();

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return new LoginResponse(jwtToken, username);
    }
}
