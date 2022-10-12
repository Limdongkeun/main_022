package main.wheelmaster;

import io.swagger.models.HttpMethod;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WheelMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WheelMasterApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*")
						.allowedMethods(HttpMethod.PATCH.name(), HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name())
						.allowedHeaders("*")
						.maxAge(3600)
						.allowCredentials(false);
			}
		}; return webMvcConfigurer;
	}
}