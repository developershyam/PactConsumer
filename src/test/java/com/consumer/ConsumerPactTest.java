package com.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.consumer.service.ConsumerService;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "test_provider")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsumerPactTest {

	@Autowired
	private ConsumerService consumerService;

	@Pact(consumer = "test_consumer", provider = "test_provider")
	public RequestResponsePact getUserPact(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return builder.given("get users").uponReceiving("GET REQUEST").path("/getUsers").method("GET").willRespondWith()
				.status(200).headers(headers).body("[\"Mike\", \"Jon\"]").toPact();
	}

	@Test
	@PactTestFor(pactMethod = "getUserPact")
	public void shouldReturnUsersWhenCallGetUsers(MockServer mockServer) {

		// when
		ResponseEntity<String> response = new RestTemplate().getForEntity(mockServer.getUrl() + "/getUsers",
				String.class);

		// then
		assertEquals(response.getStatusCode().value(), 200);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getHeaders().get("Content-Type").contains("application/json")).isTrue();
		assertThat(response.getBody()).contains("Mike", "Jon");
	}

	@Test
	void testUsers(MockServer mockServer) {

		setServerBaseUrl(mockServer);
		String users[] = consumerService.getUsers();
		assertEquals(2, users.length);
		assertEquals("Mike", users[0]);
		assertEquals("Jon", users[1]);
	}

	private void setServerBaseUrl(MockServer mockServer) {
		try {
			Field providerPath = consumerService.getClass().getDeclaredField("providerPath");
			providerPath.setAccessible(true);
			providerPath.set(consumerService, mockServer.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
