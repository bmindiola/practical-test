package com.example.app;

import com.example.app.application.dto.inbound.UpdateUserRequest;
import com.example.app.application.dto.inbound.UserRequest;
import com.example.app.application.port.inbound.UserService;
import com.example.app.application.port.outbound.ExternalApiService;
import com.example.app.application.port.outbound.UserRepository;
import com.example.app.domain.model.User;
import com.example.app.infrastructure.adapter.persistence.repository.UserJpaRepository;
import com.example.app.infrastructure.adapter.rest.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() {
		userJpaRepository.deleteAll();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).dispatchOptions(true).build();
	}

	@Test
	void testCreateUser() throws Exception {
		UserRequest userRequest = new UserRequest();
		userRequest.setUsername("testuser");
		userRequest.setEmail("testuser@example.com");
		userRequest.setPassword("Password123!");

		MvcResult result = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(status().isCreated())
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		User createdUser = objectMapper.readValue(jsonResponse, User.class);

		System.out.println("Created User: " + createdUser);

		Optional<User> savedUser = userRepository.findById(createdUser.getId());
		assertThat(savedUser).isPresent();
		assertThat(savedUser.get().getEmail()).isEqualTo("testuser@example.com");
	}

	@Test
	void testGetUserById() throws Exception {
		User user = User.builder()
				.username("testuserget")
				.email("testuserget@example.com")
				.password("Password123!")
				.build();

		User savedUser = userRepository.save(user);

		mockMvc.perform(get("/api/users/{id}", savedUser.getId())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("testuserget@example.com"))
				.andExpect(jsonPath("$.name").value("testuserget"));
	}

	@Test
	void testUpdateUser() throws Exception {
		User user = User.builder()
				.username("testuserupdate")
				.email("testuserupdate@example.com")
				.password("Password123!")
				.build();

		User savedUser = userRepository.save(user);

		UpdateUserRequest updatedUserRequest = new UpdateUserRequest();
		updatedUserRequest.setUsername("updatedUser");
		updatedUserRequest.setEmail("updateduser@example.com");

		mockMvc.perform(put("/api/users/{id}", savedUser.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedUserRequest)))
				.andExpect(status().isOk());

		Optional<User> updatedUser = userRepository.findById(savedUser.getId());
		assertThat(updatedUser).isPresent();
		assertThat(updatedUser.get().getEmail()).isEqualTo("updateduser@example.com");
		assertThat(updatedUser.get().getUsername()).isEqualTo("updatedUser");
	}

	@Test
	void testDeleteUser() throws Exception {
		User user = User.builder()
				.username("testuserdelete")
				.email("testuserdelete@example.com")
				.password("Password123!")
				.build();

		User savedUser = userRepository.save(user);

		mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
				.andExpect(status().isNoContent());

		Optional<User> deletedUser = userRepository.findById(savedUser.getId());
		assertThat(deletedUser).isNotPresent();
	}
}
