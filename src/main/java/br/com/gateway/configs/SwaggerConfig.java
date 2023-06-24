package br.com.gateway.configs;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private final RouteDefinitionLocator locator;

    public SwaggerConfig(final RouteDefinitionLocator locator) {
        this.locator = locator;
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*_route")).forEach(routeDefinition -> {
            String name = routeDefinition.getId().replaceAll("_route", "");
            GroupedOpenApi api = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            groups.add(api);
        });
        return groups;
    }
}
