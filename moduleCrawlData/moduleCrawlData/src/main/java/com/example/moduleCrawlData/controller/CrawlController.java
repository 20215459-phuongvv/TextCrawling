package com.example.moduleCrawlData.controller;

import com.example.moduleCrawlData.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CrawlController {

    @Autowired
    private CrawlService crawlService;

    @GetMapping("/crawl-data")
    public ResponseEntity<String> crawlData() {
        // Start the asynchronous API calls
        crawlService.crawlApi1();
        crawlService.crawlApi2();

        // Return the success response immediately
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/crawl-status")
    public ResponseEntity<String> checkStatus() {
        try {
            // Start the asynchronous status checks
            CompletableFuture<String> status1 = crawlService.checkStatusApi1();
            CompletableFuture<String> status2 = crawlService.checkStatusApi2();

            // Combine the results once both are completed
            CompletableFuture.allOf(status1, status2).join();

            String combinedStatus = status1.get() + "\n" + status2.get();
            return ResponseEntity.ok(combinedStatus);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking statuses: " + e.getMessage());
        }
    }
}
