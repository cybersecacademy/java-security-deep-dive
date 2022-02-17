package edu.otib.lab_deserialization;

import edu.otib.lab_deserialization.data.User;
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
public class LabDeserializationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testJsonDeserialization() throws Exception{
        ResponseEntity<String> response = restTemplate.
                getForEntity("/ois?session=eyJpZCI6LTE1MjM3MzM0NjEsIm5hbWUiOiJ0ZXN0In0=", String.class);

        assertThat(response.getBody()).contains("r{id=-1523733461, name=&#39;test&#39;}");
    }
}
