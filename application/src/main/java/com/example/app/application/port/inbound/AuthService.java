package com.example.app.application.port.inbound;

import com.example.app.application.dto.inbound.LoginRequest;
import com.example.app.application.dto.outbound.LoginResponse;

public interface AuthService {

    LoginResponse authenticate(LoginRequest loginRequest);
}
