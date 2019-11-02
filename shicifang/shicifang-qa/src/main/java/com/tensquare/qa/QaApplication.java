package com.tensquare.qa;

import com.tensquare.qa.cse.FriendRestRemoteServiceImpl;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import util.IdWorker;

@EnableServiceComb
@SpringBootApplication
public class QaApplication {
	public static void main(String[] args) {
		SpringApplication.run(QaApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}

}
