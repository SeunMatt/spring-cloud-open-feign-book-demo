package com.smattme.democonsumer.integrations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Test
	void givenCustom_whenErrorPoint_thenRaiseCustomException() throws Exception {

		// the returned JSON response body is of this format
		// {"code":400,"message":"CustomApplicationException
		// raised","errors":["CustomApplicationException raised"],"status":false}

		mvc.perform(get("/demo/errorpoint").queryParam("errorKind", "custom")).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.status", Matchers.equalTo(false)))
				.andExpect(jsonPath("$.code", Matchers.equalTo(400)))
				.andExpect(jsonPath("$.message", Matchers.equalTo("CustomApplicationException raised")));

	}

	@Test
	void whenErrorPoint_thenRaiseRuntimeException() throws Exception {

		// the returned JSON response body is of this format
		// {"code":400,"message":"CustomApplicationException
		// raised","errors":["RuntimeException raised"],"status":false}

		mvc.perform(get("/demo/errorpoint")).andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.status", Matchers.equalTo(false)))
				.andExpect(jsonPath("$.code", Matchers.equalTo(500)))
				.andExpect(jsonPath("$.message", Matchers.equalTo("Something went wrong internally")));

	}

}
