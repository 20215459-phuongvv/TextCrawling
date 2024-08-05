package com.viettel.vht.controllers;

import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import com.viettel.vht.services.interfaces.VietnamnetCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    @Autowired
    private DanTriCrawlService danTriCrawlService;
    @Autowired
    private VietnamnetCrawlService vietnamnetCrawlService;
    private final AtomicBoolean crawling = new AtomicBoolean(false);
    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }
    @GetMapping("/crawl/state")
    public boolean checkCrawlingState() {
        return crawling.get();
    }
    @GetMapping("/all-apis")
    public String callAPI() throws IOException {
        try {
            if (crawling.compareAndSet(false, true)) {
                CompletableFuture<List<PostEntity>> result1 = danTriCrawlService.crawlDanTriAsync();
                CompletableFuture<List<PostEntity>> result2 = vietnamnetCrawlService.crawlVietnamnetAsync();
                CompletableFuture.allOf(result1, result2).thenRun(() -> crawling.set(false));
                return "Crawling started.";
            } else {
                danTriCrawlService.stopCrawling();
                vietnamnetCrawlService.stopCrawling();
                crawling.set(false);
                return "Crawling stopped.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            crawling.set(false);
            return "Error occurred while calling APIs";
        }
    }
}
