package net.simforge.networkview.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	// todo AK health-check - is alive (how often refresher works for both networks)
	// todo AK health-check - is the last update age more than 10 mins
}
