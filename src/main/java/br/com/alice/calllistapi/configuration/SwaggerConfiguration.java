package br.com.alice.calllistapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	
	/*
	 * URL: http://localhost:8080/call-list/swagger-ui.html 
	 */
	
	@Value("${app.version}")
	private String appVersion;
	
	@Value("${app.name}")
	private String appName;
	
	@Value("${app.description}")
	private String appDescription;
    
	@Bean
    public Docket api() { 
		
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("br.com.alice.calllistapi.controllers.rest")) //.apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any()) //PathSelectors.ant("/api/v1/*")
          .build()
          .apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "API DOCUMENTATION: CALL-LIST", 
	      appName + " - " + appDescription, 
	      appVersion, 
	      "Terms and Services", 
	      new Contact("Thiago Ito", "www.example.com", "myeaddress@company.com"), 
	      "License of API", 
	      "API license URL", 
	      Collections.emptyList());
	}
}