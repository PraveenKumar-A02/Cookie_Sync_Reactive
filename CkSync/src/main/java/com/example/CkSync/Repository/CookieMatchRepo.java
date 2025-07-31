package com.example.CkSync.Repository;

import com.example.CkSync.Model.CookieMatch;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface CookieMatchRepo extends ReactiveElasticsearchRepository<CookieMatch,String> {

}
