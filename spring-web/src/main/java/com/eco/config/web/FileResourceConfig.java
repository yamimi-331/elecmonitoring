package com.eco.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//업로드 파일 경로를 웹에서 접근 가능하도록 설정하는 설정 클래스
@Configuration
public class FileResourceConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// C:/upload/ 경로의 파일을 /files/** URL로 접근 가능하게 매핑
		registry.addResourceHandler("/files/**").addResourceLocations("file:///D:/01-STUDY/SHJ/upload/");
	}
}
