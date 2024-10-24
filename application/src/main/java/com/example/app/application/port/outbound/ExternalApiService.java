package com.example.app.application.port.outbound;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface ExternalApiService {

    JsonNode fetchExternalData(String username) throws IOException;
}
