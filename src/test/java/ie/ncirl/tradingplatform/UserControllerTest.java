package ie.ncirl.tradingplatform;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradingplatformApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
	@LocalServerPort
	 int randomServerPort;
	 String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNTkzNjczMzkxLCJpYXQiOjE1ODU4OTczOTF9.JQN3xQfEC-cjxEKTlKaWKqJl4RQOrdKgfT7UPvXhA3_yWFg8PHUDUNdJYkyWcqlYhrQS2s-lxajaZcEf5aM2TA";
	 
	 @Autowired
	 MockMvc mockMvc;
	 
	 @Test
		public void testShowProfile() throws Exception {
		    mockMvc.perform(get("/user/profile").header("Authorization", token)).andExpect(status().isOk());
		}
	 @Test
		public void testRegister() throws Exception {
		    mockMvc.perform(post("/user/register").header("Authorization", token).param("username", "root").param("displayName", "test").param("password", "test")).andExpect(status().is4xxClientError());
		}
	 @Test
		public void testCreateAuthenticationToken() throws Exception {
		    mockMvc.perform(post("/user/authenticate").header("Authorization", token).param("username", "root").param("password", "password")).andExpect(status().is4xxClientError());
		}
}
