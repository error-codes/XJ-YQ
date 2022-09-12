package com.young.xjyq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 14:29
 */
@Configuration
public class SwaggerConfig {

    private Docket createSwagger() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.young.xjyq.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("新疆舆情 API")
                .contact(new Contact(
                        "error-codes [BayMax]",
                        null,
                        "2309590834@qq.com"))
                .description("新疆舆情 项目接口文档")
                .version("0.0.1-SNAPSHOT").build();
    }

    @Bean
    public Docket createRestApi() {
        return createSwagger();
    }
}
