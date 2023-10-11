package io.oopy.coding.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8081", description = "Local Server"),
                @Server(url = "${palco.domain.backend}", description = "Remote Server"),
        }
)
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Environment environment;

    @Bean
    public OpenAPI openAPI() {
        String activeProfile = "";
        if (!ObjectUtils.isEmpty(environment.getActiveProfiles()) &&
                environment.getActiveProfiles().length >= 1) {
            activeProfile = environment.getActiveProfiles()[0];
        }

        var securitySchemeAccessToken = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization");

        Info info = new Info()
                .title("80000 Coding API (" + activeProfile + ")")
                .description("80000 Coding API Docs")
                .version("1.0.0");

        SecurityRequirement schemaRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url(""))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securitySchemeAccessToken)
                )
                .security(List.of(schemaRequirement))
                .info(info);
    }
}