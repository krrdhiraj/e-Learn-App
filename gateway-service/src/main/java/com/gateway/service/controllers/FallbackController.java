package com.gateway.service.controllers;

import com.gateway.service.dtos.CategoryFailDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class FallbackController {
    @RequestMapping("/categoryFallbackUri")
    public Mono<CategoryFailDto> categoryFallback() {
        CategoryFailDto categoryFailDto = new CategoryFailDto("category service is not available.", false);
        return Mono.just(categoryFailDto);
    }

    @RequestMapping("/courseFallback")
    public Mono<String> courseFallback(){
        return Mono.just("Course Service is not available");
    }
}
