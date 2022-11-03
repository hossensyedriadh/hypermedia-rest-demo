package io.github.hossensyedriadh.restdemo.configuration.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
@EnableOpenApi
@Import({BeanValidatorPluginsConfiguration.class})
@PropertySource("classpath:application.properties")
public class SwaggerConfiguration {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${info.application.metadata.description}")
    private String appDescription;

    @Value("${info.application.metadata.version}")
    private String appVersion;

    @Value("${info.application.developer.name}")
    private String devName;

    @Value("${info.application.developer.url}")
    private String devUrl;

    @Value("${info.application.developer.email}")
    private String devEmail;

    @Value("${server.servlet.context-path}")
    private String appContextPath;

    @Bean
    public Docket swaggerUiConfiguration() {
        Set<String> protocols = new HashSet<>();
        protocols.add("https");

        Set<String> consumedTypes = new HashSet<>();
        consumedTypes.add(MediaType.APPLICATION_JSON_VALUE);

        Set<String> producedTypes = new HashSet<>();
        producedTypes.add(MediaType.APPLICATION_JSON_VALUE);
        producedTypes.add(MediaTypes.HAL_JSON_VALUE);
        producedTypes.add(MediaTypes.HAL_FORMS_JSON_VALUE);

        return new Docket(DocumentationType.OAS_30)
                .securityContexts(List.of(this.securityContext()))
                .securitySchemes(List.of(this.apiKey()))
                .select().apis(RequestHandlerSelectors.basePackage("io.github.hossensyedriadh.restdemo.controller"))
                .paths(PathSelectors.ant("/" + this.appContextPath + "/**")).build()
                .apiInfo(this.metadata()).protocols(protocols).consumes(consumedTypes).produces(producedTypes);
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    private ApiInfo metadata() {
        return new ApiInfo(this.appName, this.appDescription, this.appVersion, null, new Contact(this.devName, this.devUrl, this.devEmail),
                null, null, Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(this.securityReferences()).build();
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "Access secured endpoints");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return List.of(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes));
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier,
                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
