package com.young.xjyq;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableScheduling
@EnableOpenApi
@EnableKnife4j
@ForestScan(basePackages = "com.young.xjyq.service")
public class XjyqApplication {

	public static void main(String[] args) {
		SpringApplication.run(XjyqApplication.class, args);
	}

}
