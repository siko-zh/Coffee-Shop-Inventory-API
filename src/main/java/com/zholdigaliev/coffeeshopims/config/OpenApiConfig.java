package com.zholdigaliev.coffeeshopims.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coffeeShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CoffeeShopIMS API")
                        .description("Система управления запасами для кофейни.")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Вставь JWT токен из /auth/login")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }

    @Bean
    public OperationCustomizer securityRolesCustomizer() {
        return (operation, handlerMethod) -> {
            PreAuthorize preAuth = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            if (preAuth != null) {
                List<String> roles = extractRoles(preAuth.value());
                if (!roles.isEmpty()) {
                    String desc = operation.getDescription();
                    String roleNote = "\n\n🔐 **Требуемые роли:** " + String.join(", ", roles);
                    operation.setDescription((desc != null ? desc : "") + roleNote);
                }
            }
            return operation;
        };
    }

    private List<String> extractRoles(String expression) {
        List<String> roles = new ArrayList<>();
        Matcher m = Pattern.compile("has(?:Any)?Role\\('([^']+)'\\)").matcher(expression);
        while (m.find()) {
            roles.add(m.group(1));
        }
        return roles;
    }
}
