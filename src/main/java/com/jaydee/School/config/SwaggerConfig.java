package com.jaydee.School.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	 @Bean
	    public GroupedOpenApi publicApi() {
	        return GroupedOpenApi.builder()
	            .group("public")
	            .pathsToMatch("/**")
	            .build();
	    }
	
//   public Docket SwaggerApi() {
//       return new Docket(DocumentationType.SWAGGER_2)
//               .select()
//               .apis(RequestHandlerSelectors.any())
//               .paths(PathSelectors.any())
//               .build();
//   }
//	
//    @Bean
//    public InternalResourceViewResolver defaultViewResolver() {
//    	return new InternalResourceViewResolver();
//    }
	 @Bean
	    public OpenAPI apiInfo() {
	        return new OpenAPI()
	                .info(new Info()
	                        .title("School API")
	                        .description("API Documentation for School Application")
	                        .version("1.0.0"));
	    }
	
	
	
}
