package com.kjz.www;

import com.kjz.www.listener.SessionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ImportResource(value = {"classpath:spring-*.xml"})
@MapperScan("com.kjz.www.admin.mapper.IAdminMapper")
public class ArticleApplication{

	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}

	//设置监听器
	@Bean
	public ServletListenerRegistrationBean<SessionListener> sessionListenerServletListenerRegistrationBean(){
		ServletListenerRegistrationBean<SessionListener> registration = new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
		return registration;
	}


}
