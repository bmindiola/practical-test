package com.example.app.domain.service;

import com.example.app.application.port.outbound.ExternalApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GitHubServiceImpl implements ExternalApiService {

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.external.url}")
    private String externalApiUrl;

    @Override
    public JsonNode fetchExternalData(String username) throws IOException {
        String url = externalApiUrl + username;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            ResponseBody responseBody = response.body();

            if (responseBody == null)
                throw new IOException("Response body is null");

            return objectMapper.readTree(responseBody.string());
        }
    }
}
