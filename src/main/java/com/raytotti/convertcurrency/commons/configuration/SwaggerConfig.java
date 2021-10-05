package com.raytotti.convertcurrency.commons.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.raytotti.convertcurrency"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Convert Currency")
                        .description("API Rest that is able to perform a conversion between two currencies using updated rates from an external service.")
                        .version("1.0.0")
                        .contact(new Contact("Ray Toti Felix de Araujo", "https://github.com/raytotti/convertcurrency", "raytottifa@hotmail.com"))
                        .build());

    }
}
