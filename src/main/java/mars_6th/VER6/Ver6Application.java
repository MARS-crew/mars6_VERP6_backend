package mars_6th.VER6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Ver6Application {

	public static void main(String[] args) {
		SpringApplication.run(Ver6Application.class, args);
	}

}
