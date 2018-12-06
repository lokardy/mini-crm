package com.mycompany.minicrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan( basePackages = {"com.audibene.crm.entity"} )
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("com.audibene.crm.repository")
@ComponentScan(basePackages = {"com.audibene.crm.service.impl", "com.audibene.crm.advice", "com.audibene.crm.controller"})
public class MiniCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniCrmApplication.class, args);
	}
}
