package com.example.CkSync.Controller;


import com.example.CkSync.Service.CkSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/cksync")
public class CkSyncController {

    @Autowired
    private CkSyncService service;

    @GetMapping("/sync")
    public Mono<Void> sync(@RequestParam("mro_cookie_id") String mroCookieId, @CookieValue(value = "mro_sync_id", required = false) String mroSyncId, ServerHttpResponse response) {
        if(mroSyncId==null) {
            mroSyncId = UUID.randomUUID().toString();
            ResponseCookie cookie = ResponseCookie.from("mro_sync_id", mroSyncId)
                    .maxAge(Duration.ofDays(365))
                    .path("/")
                    .httpOnly(true)
                    .build();
            response.addCookie(cookie);

        }
        return service.sync(mroSyncId,mroCookieId)
        .then(Mono.fromRunnable(() -> {
                String redirect = "http://localhost:8084/mro/adx/sspcall?redirect=http://localhost:8082/cksync/adx/callback";
                        response.setStatusCode(HttpStatus.FOUND);
                        response.getHeaders().setLocation(URI.create(redirect));

                    }))
                         .then(response.setComplete());
        }


    @GetMapping("/adx/callback")
    public Mono<Void> adxReceiveCallback(@CookieValue("mro_sync_id") String syncId,
                                         @RequestParam String adx_id,
                                         ServerHttpResponse res) {

        System.out.println("SYNC CALLBACK: " + syncId + ", ADX: " + adx_id);
        return service.adxCookie(syncId, adx_id)
                .delayElement(Duration.ofMillis(500))
                .then(Mono.fromRunnable(() -> {
                    res.setStatusCode(HttpStatus.FOUND);
                    res.getHeaders().setLocation(URI.create("http://localhost:8084/mro/fluct/sspcall?redirect=http://localhost:8082/cksync/fluct/callback"));

                }))
                .then(res.setComplete());
    }



    @GetMapping("/fluct/callback")
    public Mono<String> fluctReceiveCallback(@CookieValue("mro_sync_id")String syncId,
                                     @RequestParam String fluct_id,ServerHttpResponse response) {


             return service.fluctCookie(syncId,fluct_id)
                     .then(Mono.just("Success"));

    }

}

