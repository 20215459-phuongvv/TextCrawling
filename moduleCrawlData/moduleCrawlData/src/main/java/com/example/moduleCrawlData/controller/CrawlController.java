package com.example.moduleCrawlData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CrawlController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/crawl-data")
    public ResponseEntity<String> crawlData() {
        String api1Url = "https://vht-repo-v1-0.onrender.com/api/v1/all-apis";
        String api2Url = "https://vhtcrawldata.onrender.com/start_crawl";

        // Call the first API
        String response1 = restTemplate.getForObject(api1Url, String.class);

        // Call the second API
        String response2 = restTemplate.getForObject(api2Url, String.class);

        // Combine the responses or process as needed
        String combinedResponse = "API 1 Response: " + response1 + "\nAPI 2 Response: " + response2;

        return ResponseEntity.ok(combinedResponse);
    }
}
