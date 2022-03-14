package com.nutcracker.config;


import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.springdoc.core.Constants.ALL_PATTERN;


@Configuration
public class SpringDocConfig {

    @Value("${app.version}")
    private String appversion;

    @Bean
    @Profile("!pro")
    public GroupedOpenApi actuatorApi(OpenApiCustomiser actuatorOpenApiCustomiser,
                                      OperationCustomizer actuatorCustomizer,
                                      WebEndpointProperties endpointProperties) {
        return GroupedOpenApi.builder()
                .group("Actuator")
                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
                .addOpenApiCustomiser(actuatorOpenApiCustomiser)
                .addOpenApiCustomiser(openApi -> openApi.info(new io.swagger.v3.oas.models.info.Info().title("Actuator API").version(appversion)))
                .addOperationCustomizer(actuatorCustomizer)
                .pathsToExclude("/health/*")
                .build();
    }

    @Profile("!pro")
    @Bean
    public GroupedOpenApi secretGroup() {
        return GroupedOpenApi.builder()
                .group("加解密")
                //.pathsToMatch("/secret/*")
                .packagesToScan("com.nutcracker.web.controller.secret")
                .addOpenApiCustomiser(openApi -> openApi.info(
                        new io.swagger.v3.oas.models.info.Info()
                                .title("加解密")
                                .description("加密解密相关接口分组")
                                .version(appversion)))
                .build();
    }


}

