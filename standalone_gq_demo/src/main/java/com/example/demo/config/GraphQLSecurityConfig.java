package com.example.demo.config;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLSecurityConfig {

    @Bean
    public Instrumentation maxQueryDepthInstrumentation() {
        // Ограничиваем максимальную глубину запроса: 20 уровней
        // (достаточно для GraphiQL Introspection, блокирует бесконечную рекурсию)
        return new MaxQueryDepthInstrumentation(20);
    }

    @Bean
    public Instrumentation maxQueryComplexityInstrumentation() {
        // Ограничиваем максимальную сложность запроса: 200 единиц
        // (по умолчанию каждое запрашиваемое поле "весит" 1 единицу)
        return new MaxQueryComplexityInstrumentation(200);
    }
}
