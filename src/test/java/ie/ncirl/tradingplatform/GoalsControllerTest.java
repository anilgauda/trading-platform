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
public class GoalsControllerTest {
	 @LocalServerPort
	 int randomServerPort;
	 String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNTkzNjczMzkxLCJpYXQiOjE1ODU4OTczOTF9.JQN3xQfEC-cjxEKTlKaWKqJl4RQOrdKgfT7UPvXhA3_yWFg8PHUDUNdJYkyWcqlYhrQS2s-lxajaZcEf5aM2TA";
	 
	 @Autowired
	 MockMvc mockMvc;
	 
	 @Test
		public void testCreateGoals() throws Exception {
		    mockMvc.perform(post("/app/goal").header("Authorization", token).param("name","goal1").param("startDate","03/04/2020").param("endDate","03/04/2020").param("targetAmount","100").param("percent","10")).andExpect(status().isOk());
		  
		}
	 
	 @Test
		public void testGetGoals() throws Exception {
		    mockMvc.perform(get("/app/goal").header("Authorization", token)).andExpect(status().isOk());
		  
		}
	 @Test
		public void testDeleteGoals() throws Exception {
		    mockMvc.perform(get("/app/goal/").header("Authorization", token).param("goalId", "1")).andExpect(status().isOk());
		  
		}
}
