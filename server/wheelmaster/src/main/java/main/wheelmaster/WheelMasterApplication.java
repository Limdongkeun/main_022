package main.wheelmaster;

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
				registry.addMapping("/**").allowedOrigins("http://main-project-22.s3-website.ap-northeast-2.amazonaws.com");
			}
		}; return webMvcConfigurer;
	}
}