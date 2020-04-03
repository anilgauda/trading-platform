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
public class SharesControllerTest {
	 @LocalServerPort
	 int randomServerPort;
	
	 String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNTkzNjczMzkxLCJpYXQiOjE1ODU4OTczOTF9.JQN3xQfEC-cjxEKTlKaWKqJl4RQOrdKgfT7UPvXhA3_yWFg8PHUDUNdJYkyWcqlYhrQS2s-lxajaZcEf5aM2TA"; 
	 @Autowired
	 MockMvc mockMvc;
	@Test
	public void testGetStocks() throws Exception {
	    mockMvc.perform(get("/trade/sharedetails").header("Authorization", token).param("user","root").param("name","AMZN")).andExpect(status().isOk());
	  
	}
	
	@Test
	public void testGetMyStocks() throws Exception {
	    mockMvc.perform(get("/trade/my").header("Authorization", token)).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllStocks() throws Exception {
	    mockMvc.perform(get("/trade/all").header("Authorization", token)).andExpect(status().isOk());
	}
	
	@Test
	public void testGetMyTransactions() throws Exception {
	    mockMvc.perform(get("/trade/history").header("Authorization", token)).andExpect(status().isOk());
	}
	
	@Test
	public void testBuyStock() throws Exception {
	    mockMvc.perform(post("/trade/share/buy").header("Authorization", token).param("symbol", "AMZN").param("price", "100").param("quantity","1")).andExpect(status().isOk());
	}
	
	@Test
	public void testSellStock() throws Exception {
	    mockMvc.perform(post("/trade/share/sell").header("Authorization", token).param("symbol", "AMZN").param("price", "100").param("quantity","1")).andExpect(status().isOk());
	}
	
}
