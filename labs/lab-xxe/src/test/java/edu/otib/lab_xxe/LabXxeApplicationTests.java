package edu.otib.lab_xxe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LabXxeApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testXMLProcessing() {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("bill_xml", new org.springframework.core.io.ClassPathResource("bill_normal.xml"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

		ResponseEntity<String> response = restTemplate.exchange("/bill", HttpMethod.POST, entity, String.class,
				"");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("31337");
	}

	/*
	// Security test
	@Test
	public void testXXEDefense() throws Exception{
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("bill_xml", new org.springframework.core.io.ClassPathResource("bill_entity.xml"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

		ResponseEntity<String> response = restTemplate.exchange("/bill", HttpMethod.POST, entity, String.class,
				"");

		assertThat(response.getBody()).doesNotContain("XXETESTXXE");
	}*/

}
