package com.example.Mro.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/mro")
public class MroController {

    @GetMapping("/visit")
    public Mono<String> handleVisit(@CookieValue(value = "mro_cookie_id", required = false) String mroCookieId, ServerHttpResponse response) {
        if (mroCookieId == null) {
            mroCookieId = "mro_" + UUID.randomUUID().toString();
            ResponseCookie cookie= ResponseCookie.from("mro_cookie_id",mroCookieId)
                    .path("/")
                    .maxAge(365*24*60*60)
                    .httpOnly(true)
                    .build();
            response.addCookie(cookie);
        }
        System.out.println("mro" + mroCookieId);
        String html="<html><body> WELCOME TO MRO" + "<img src='http://localhost:8082/cksync/sync?mro_cookie_id=" + mroCookieId + "'width='1' height='1'/>" + "</body></html>";
        return Mono.just(html);
    }

    @GetMapping("adx/sspcall")
    public Mono<Void> adxsspcall(ServerHttpResponse res,@RequestParam("redirect") String redirect){

        String redirect1="http://localhost:8081/adx/sync?redirect=http://localhost:8084/mro/adx/callback&fredirect="+redirect;
        res.setStatusCode(HttpStatus.FOUND);
        res.getHeaders().setLocation(URI.create(redirect1));
        return res.setComplete();

    }

    @GetMapping("fluct/sspcall")
    public Mono<Void> fluctsspcall(ServerHttpResponse res,@RequestParam("redirect") String redirect){

        String redirect1="http://localhost:8083/fluct/sync?redirect=http://localhost:8084/mro/fluct/callback&fredirect="+redirect;
        res.setStatusCode(HttpStatus.FOUND);
        res.getHeaders().setLocation(URI.create(redirect1));
        return res.setComplete();

    }
    @GetMapping("fluct/callback")
    public Mono<Void> callback(ServerHttpResponse res,@RequestParam String fredirect,@RequestParam(required = false) String fluct_id){

        String separator = fredirect.contains("?") ? "&" : "?";
        String redirect = fredirect + separator + "fluct_id=" + fluct_id;
        res.setStatusCode(HttpStatus.FOUND);
        res.getHeaders().setLocation(URI.create(redirect));
        return res.setComplete();


    }
    @GetMapping("adx/callback")
    public Mono<Void> adxcallback(ServerHttpResponse res,@RequestParam String fredirect,@RequestParam(required = false) String adx_id){

        String separator = fredirect.contains("?") ? "&" : "?";
        String redirect = fredirect + separator + "adx_id=" + adx_id;
        res.setStatusCode(HttpStatus.FOUND);
        res.getHeaders().setLocation(URI.create(redirect));
        return res.setComplete();

    }

}



