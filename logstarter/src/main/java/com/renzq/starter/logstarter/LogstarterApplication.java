package com.renzq.starter.logstarter;

import com.renzq.configure.annotation.beanregister.EnableAutoPerfLog;
import com.renzq.configure.annotation.imp.EnablePerfLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@EnablePerfLog
@SpringBootApplication
public class LogstarterApplication {

	@Bean
	StartupRunner startupRunner(){
		return new StartupRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(LogstarterApplication.class, args);
	}
}
