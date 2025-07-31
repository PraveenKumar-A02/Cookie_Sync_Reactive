package com.example.CkSync.Service;

import com.example.CkSync.Model.CookieMatch;
import com.example.CkSync.Repository.CookieMatchRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
public class CkSyncService {

    private CookieMatchRepo repo;
    public CkSyncService(CookieMatchRepo repo){
        this.repo=repo;
    }

    public Mono<Void> sync(String mroSyncId,String mroCookieId) {

       return  repo.findById(mroSyncId)
                .switchIfEmpty(Mono.just(new CookieMatch(mroSyncId)))
                        .flatMap(match -> {
                            if (match.getMroCookieId() == null) {
                                match.setMroCookieId(mroCookieId);
                            }
                            match.setTimestamp(new Date());
                            return repo.save(match);
                        } ).then();


    }


    public Mono<Void> fluctCookie(String mroSyncId, String fluctId) {
        return Mono.defer(()->repo.findById(mroSyncId))
                .switchIfEmpty(Mono.error(new RuntimeException("Error in fluct" + mroSyncId)))
                                .flatMap(current -> {
                                    current.setFluctCookieId("fluct_" + fluctId); // update only adx
                                    current.setTimestamp(new Date());
                                    System.out.println("Saving updated (fluct only): " + current);
                                    return repo.save(current);
                                })
                 .then();
    }


    public Mono<Void> adxCookie(String mroSyncId, String adxId) {
        return Mono.defer(()->repo.findById(mroSyncId))

                .switchIfEmpty(Mono.error(new RuntimeException("Error in adx" + mroSyncId)))
                                .flatMap(current -> {
                                    current.setAdxCookieId("adx_" + adxId);
                                    current.setTimestamp(new Date());
                                    System.out.println("Saving updated (adx only): " + current);
                                    return repo.save(current);
                                })
                .then();
    }



}

