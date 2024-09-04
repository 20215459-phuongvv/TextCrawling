package com.example.moduleCrawlData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class CrawlService {

    @Autowired
    private RestTemplate restTemplate;

    private String api1Url = "https://vht-repo-v1-0.onrender.com/api/v1";
    private String api2Url = "https://vhtcrawldata.onrender.com";

    @Async
    public CompletableFuture<String> crawlApi1() {
        String response = restTemplate.getForObject(api1Url + "/all-apis", String.class);
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<String> crawlApi2() {
        String response = restTemplate.getForObject(api2Url + "/start_crawl", String.class);
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<String> checkStatusApi1() {
        String response = restTemplate.getForObject(api1Url + "/crawl/state", String.class);
        return CompletableFuture.completedFuture("API 1 Status: " + response);
    }

    @Async
    public CompletableFuture<String> checkStatusApi2() {
        String response = restTemplate.getForObject(api2Url + "/status", String.class);
        return CompletableFuture.completedFuture("API 2 Status: " + response);
    }
}
