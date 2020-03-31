package ie.ncirl.tradingplatform;

import com.amazonaws.SDKGlobalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradingplatformApplication {

	public static void main(String[] args) {
		System.setProperty(SDKGlobalConfiguration.AWS_EC2_METADATA_DISABLED_SYSTEM_PROPERTY, "true");
		SpringApplication.run(TradingplatformApplication.class, args);
	}

}
