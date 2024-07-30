package com.viettel.vht.controllers;

import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import com.viettel.vht.services.interfaces.VietnamnetCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/crawl")
public class VietnamnetCrawlController {
    @Autowired
    private VietnamnetCrawlService vietnamnetCrawlService;
    @GetMapping ("/vietnamnet")
    public List<PostEntity> crawlVietnamnet() throws IOException {
        return vietnamnetCrawlService.crawlVietnamnet(false);
    }
    @GetMapping("/vietnamnet/single")
    public PostEntity crawlOneVietnamnetPost(@RequestParam("url") String url) throws IOException {
        return vietnamnetCrawlService.crawlOneVietnamnetPost(url);
    }
}
