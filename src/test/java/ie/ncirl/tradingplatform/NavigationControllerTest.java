package ie.ncirl.tradingplatform;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradingplatformApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class NavigationControllerTest{

	 @LocalServerPort
	 int randomServerPort;
	
	@Test
	public void testShowDashboard() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/";
	    URI uri = new URI(baseUrl);
	    
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("Stock Data"));
	}
	
	@Test
	public void testShowLogin() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/login";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("Stock Manager - Login"));
	}
	
	@Test
	public void testShowRegister() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/register";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("Stock Manager - Register"));
	}
	
	@Test
	public void testShowSharesDetail() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/trade";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("Stock Details"));
	}
	
	@Test
	public void testShowMyStocks() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/stocks";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("My Stocks"));
	}
	
	@Test
	public void testShowHistory() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/history";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("My Transactions"));
	}
	
	@Test
	public void testShowGoals() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomServerPort + "/app/goals";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result=restTemplate.getForEntity(uri, String.class);
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("Goals"));
	}

}
