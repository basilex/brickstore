package com.platform.brickstore.api.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Reusable ProblemDetail schema to document error responses
        Schema<?> problemDetail = new Schema<>();
        problemDetail.setType("object");
        Map<String, Schema<?>> props = new LinkedHashMap<>();
        props.put("type", new StringSchema().format("uri").description("Problem type URI"));
        props.put("title", new StringSchema().description("Short, human-readable summary of the problem"));
        props.put("status", new IntegerSchema().format("int32").description("HTTP status code"));
        props.put("detail", new StringSchema().description("Detailed human-readable message"));
        props.put("instance", new StringSchema().description("URI that identifies the specific occurrence"));
        props.put("timestamp", new StringSchema().format("date-time"));
        props.put("error", new StringSchema());
        props.put("details", new ObjectSchema().description("Additional machine-readable details"));
        props.put("errorCode", new StringSchema().description("Domain error code"));
        props.put("requestId", new StringSchema().description("Correlation id / request id"));

        @SuppressWarnings({"rawtypes", "unchecked"})
        Map rawProps = (Map) props;
        problemDetail.setProperties(rawProps);

        Components components = new Components().addSchemas("ProblemDetail", problemDetail);

        Info info = new Info()
            .title("Brickstore API")
            .version("0.1.0")
            .description("Brickstore REST API")
            .contact(new Contact().name("Platform Team").email("platform@example.com"))
            .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"));

        Server local = new Server().url("http://localhost:8081").description("Local development server");

        return new OpenAPI()
            .components(components)
            .servers(List.of(local))
            .info(info);
    }
}
