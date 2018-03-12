package com.kjz.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ImportResource(value = {"classpath:spring-*.xml"}) 
public class ArticleApplication {
	@Autowired
	private MultipartConfigElement multipartConfigElement;//注入 “多部件配置元素”
	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}

	/**
	 * 修改DispatcherServlet默认配置
	 *
	 */
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);

		//这里需要将附件配置设置进去，否则请求不过来 报异常：Required request part 'file' is not present
		registration.setMultipartConfig(multipartConfigElement);
		return registration;
	}
}
