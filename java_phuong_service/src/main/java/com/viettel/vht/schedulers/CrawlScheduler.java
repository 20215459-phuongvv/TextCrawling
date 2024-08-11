package com.viettel.vht.schedulers;

import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import com.viettel.vht.services.interfaces.VietnamnetCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CrawlScheduler {
    @Autowired
    private DanTriCrawlService danTriCrawlService;
    @Autowired
    private VietnamnetCrawlService vietnamnetCrawlService;

    @Scheduled(cron = "0 0 0 * * ?")
    private String scheduledCrawl() throws IOException {
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
