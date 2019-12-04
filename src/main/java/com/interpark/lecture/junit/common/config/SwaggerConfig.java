package com.interpark.lecture.junit.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${interpark.ncl.image.api.name}")
    private String apiTitle;

    @Value("${interpark.ncl.image.api.description}")
    private String apiDescription;

    @Value("${interpark.ncl.image.api.version}")
    private String apiVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiMetadata())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/lecture-junit/v{\\d+}/**"))
                .build();
    }

    private ApiInfo apiMetadata() {
        return new ApiInfoBuilder()
                .title(apiTitle)
                .description(apiDescription)
                .version(apiVersion)
                .build();
    }
}
