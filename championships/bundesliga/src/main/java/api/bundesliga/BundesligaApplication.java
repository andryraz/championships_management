package api.bundesliga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan({"controller", "entity"})
public class BundesligaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BundesligaApplication.class, args);
	}

}
