package com.viettel.vht.services.interfaces;

import com.viettel.vht.entities.PostEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VietnamnetCrawlService {
    List<PostEntity> crawlVietnamnet(boolean isScheduled) throws IOException;
    CompletableFuture<List<PostEntity>> crawlVietnamnetAsync() throws IOException;

    PostEntity crawlOneVietnamnetPost(String url) throws IOException;
    void stopCrawling();
}
