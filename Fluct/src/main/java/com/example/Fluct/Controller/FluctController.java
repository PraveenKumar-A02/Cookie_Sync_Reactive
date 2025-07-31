package com.example.Fluct.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/fluct")
public class FluctController {

    @GetMapping("/sync")
    public Mono<Void> sspRedirect(@RequestParam String redirect, @RequestParam String fredirect, @CookieValue(value = "fluct_id",required = false)String fluctId, ServerHttpResponse response) {
        if(fluctId ==null) {
            fluctId=UUID.randomUUID().toString();
                ResponseCookie cookie= ResponseCookie.from("fluct_id",fluctId)
                        .path("/")
                        .maxAge(365*24*60*60)
                        .httpOnly(true)
                        .build();
                response.addCookie(cookie);
            }
        System.out.println("fluct:"+fluctId);
        String separator = redirect.contains("?") ? "&" : "?";
        String finalRedirect = redirect + separator + "fredirect=" + fredirect + "&fluct_id=" + fluctId;

        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(finalRedirect));
        return response.setComplete();

    }

}

