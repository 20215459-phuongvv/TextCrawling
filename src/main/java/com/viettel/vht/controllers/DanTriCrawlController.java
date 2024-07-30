package com.viettel.vht.controllers;

import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/crawl")
public class DanTriCrawlController {
    @Autowired
    private DanTriCrawlService danTriCrawlService;
    @GetMapping("/dan-tri")
    public List<PostEntity> crawlDanTri() throws IOException {
        return danTriCrawlService.crawlDanTri(false);
    }
    @GetMapping("/dan-tri/single")
    public PostEntity crawlOneDanTriPost(@RequestParam("url") String url) throws IOException {
        return danTriCrawlService.crawlOneDanTriPost(url);
    }
}
