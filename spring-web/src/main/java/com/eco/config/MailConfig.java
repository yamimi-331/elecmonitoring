package com.eco.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.log4j.Log4j;


@Configuration
@Log4j
public class MailConfig {
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Bean
	public JavaMailSender mailSender() {
		System.out.println("host: "+ host);
		System.out.println("port: "+ port);
		System.out.println("username: "+username);
		System.out.println("password: "+ password);
		log.info("host: "+ host);
		log.info("port: "+ port);
		log.info("username: "+username);
		log.info("password: "+ password);
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");

		return mailSender;
	}
}
