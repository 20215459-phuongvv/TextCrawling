package com.viettel.vht.controllers;

import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import com.viettel.vht.services.interfaces.VietnamnetCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class HelloController {
    @Autowired
    private DanTriCrawlService danTriCrawlService;
    @Autowired
    private VietnamnetCrawlService vietnamnetCrawlService;
    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }
    @GetMapping("/all-apis")
    public String callAPI() throws IOException {
        try {
            CompletableFuture<List<PostEntity>> result1 = danTriCrawlService.crawlDanTriAsync();
            CompletableFuture<List<PostEntity>> result2 = vietnamnetCrawlService.crawlVietnamnetAsync();
            CompletableFuture.allOf(result1, result2).join();
            return "Crawled successfully " + (result1.get().size() + result2.get().size())  + " posts.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling APIs";
        }
    }
}
