package com.example.Adx.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/adx")
public class AdxController {

    @GetMapping("/sync")
    public Mono<Void> sspRedirect(@RequestParam String redirect, @RequestParam String fredirect, @CookieValue(value = "adx_id", required = false) String adxId, ServerHttpResponse response) {
         if(adxId==null){
             adxId=UUID.randomUUID().toString();
             ResponseCookie cookie= ResponseCookie.from("adx_id",adxId)
                     .path("/")
                     .maxAge(365*60*60*24)
                     .httpOnly(true)
                     .build();
             response.addCookie(cookie);
        }

        System.out.println(adxId);
        String separator = redirect.contains("?") ? "&" : "?";
        String finalRedirect = redirect + separator + "fredirect=" + fredirect + "&adx_id=" + adxId;
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(finalRedirect));
        return response.setComplete();


    }

}
