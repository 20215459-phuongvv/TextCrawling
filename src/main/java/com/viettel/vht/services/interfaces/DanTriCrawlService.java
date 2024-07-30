package com.viettel.vht.services.interfaces;

import com.viettel.vht.entities.PostEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DanTriCrawlService {
    List<PostEntity> crawlDanTri(boolean isScheduled) throws IOException;
    CompletableFuture<List<PostEntity>> crawlDanTriAsync() throws IOException;
    PostEntity crawlOneDanTriPost(String url) throws IOException;
}
