package com.gateway.service.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public KeyResolver keyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
    @Bean
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(10, 20, 1);
    }

    // it will route services through given route here in filters
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                // for category service
                .route(r->
                    r.path("/category/**")
                            .filters(f ->
                                    f.rewritePath("/category/?(?<segment>.*)","/${segment}")
                                            .addResponseHeader("-X-CUSTOM-HEADER", "added by Chickoo")
                                    // Microservice Resiliency thorough circuit breaker
                                            .circuitBreaker(breaker -> breaker
                                                    .setName("category-breaker")
                                                    .setFallbackUri("forward:/categoryFallbackUri")
                                            )
                           )
                            .uri("lb://category-service")
                )
                // video service
//                .route(information->[url, predicate,filter])
                .route(r->
                        r.path("/video/**")
                                .filters(f ->
                                        f.rewritePath("/video/?(?<segment>.*)","/${segment}")
                                                .retry(
                                                        retryConfig -> retryConfig
                                                                .setRetries(3)// how many times try
                                                                .setMethods(HttpMethod.GET)
                                                                .setBackoff(
                                                                        Duration.ofMillis(100), // first try after given times
                                                                        Duration.ofMillis(1000), // interval b/w tries
                                                                        2,                       // factor
                                                                        true                     // Jitter( to reduce thundering herd problem)
                                                                )
                                                )
//                                                .addResponseHeader("-X-CUSTOM-HEADER", "added by Chickoo")
//                                                .circuitBreaker(breaker-> breaker.setName("video-breaker"))
                                )
                                .uri("lb://video-service")
                )
                // course service
                .route(r->
                        r.path("/course/**")
                                .filters(f ->
                                        f.rewritePath("/course/?(?<segment>.*)","/${segment}")
                                                .requestRateLimiter(rateLimiter->  rateLimiter
                                                                .setKeyResolver(keyResolver())
                                                                .setRateLimiter(redisRateLimiter())
                                                )
//                      .circuitBreaker(breaker-> break.setName("course-breaker").setFallbackUri("forward:/courseFallback"))
                                )
                                .uri("lb://course-service")
                )
                .build();
    }
}
