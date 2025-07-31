package com.example.CkSync.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "reactive_cookie_match_table")
public class CookieMatch {


    private String mroCookieId;
    @Id
    private String mroSyncId;
    private String adxCookieId;
    private String fluctCookieId;
    private Date timestamp;
    public CookieMatch(String mroSyncId) {
        this.mroSyncId = mroSyncId;
        this.timestamp = new Date();
    }

}
