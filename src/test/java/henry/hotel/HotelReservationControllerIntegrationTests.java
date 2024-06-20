package henry.hotel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import henry.hotel.controller.HotelReservationController;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HotelReservationControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private HotelReservationController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
	
	@Test
	void greetingShouldReturnDefaultMessage() throws Exception {
		final String response = this.restTemplate.getForObject(this.getBaseURL(), String.class);
		final String expected = "<title>Hotel Login</title>";

		assertThat(response).contains(expected);
	}

	String getBaseURL() {
		return "http://localhost:" + this.port + "/";
	}
	
}
