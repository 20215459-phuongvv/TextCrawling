package com.viettel.vht.services;

import com.mongodb.client.model.Filters;
import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.repositories.PostRepository;
import com.viettel.vht.services.interfaces.VietnamnetCrawlService;
import com.viettel.vht.utils.Constants;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class VietnamnetCrawlServiceImpl implements VietnamnetCrawlService {
    @Autowired
    private PostRepository postRepository;
    private final AtomicBoolean running = new AtomicBoolean(false);
    @Override
    public List<PostEntity> crawlVietnamnet(boolean isScheduled) throws IOException {
        List<PostEntity> postEntityList = new ArrayList<>();
        if (isScheduled && !running.get()) {
            return postEntityList;
        }
        Document document = Jsoup.connect(Constants.Vietnamnet.BASE_URL).get();
        Elements li = document.getElementsByClass(Constants.Vietnamnet.UL_CLASS).first().getElementsByTag(Constants.Tag.LI);
        for (Element element : li) {
            if (element.getElementsByTag(Constants.Tag.UL).first() != null) {
                Elements liList = element
                        .getElementsByTag(Constants.Tag.UL)
                        .first()
                        .getElementsByTag(Constants.Tag.LI);
                for (Element liElem : liList) {
                    if (isScheduled && !running.get()) {
                        return postEntityList;
                    }
                    String pageUrl = liElem.getElementsByTag(Constants.Tag.A).first().absUrl(Constants.Attribute.HREF);
                    System.out.println(pageUrl);
                    List<String> pagePostUrlList = crawlVietnamnetPostUrlByTag(pageUrl, isScheduled);
                    if (pagePostUrlList != null && !pagePostUrlList.isEmpty()) {
                        for (String postUrl : pagePostUrlList) {
                            if (isScheduled && !running.get()) {
                                return postEntityList;
                            }
                            System.out.println(postUrl);
                            org.bson.Document doc = postRepository.postCollection.find(Filters.eq("url", postUrl)).first();
                            if (doc == null) {
                                PostEntity postEntity = crawlVietnamnetPost(postUrl);
                                if (postEntity != null) {
                                    postEntity.setSource(Constants.Vietnamnet.SOURCE_NAME);
                                    postRepository.save(postEntity);
                                    postEntityList.add(postEntity);
                                }
                            }
                        }
                    }
                }
            }
        }
        return postEntityList;
    }

    @Override
    @Async
    public CompletableFuture<List<PostEntity>> crawlVietnamnetAsync() throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            running.set(true);
            List<PostEntity> posts = new ArrayList<>();
            try {
                posts = crawlVietnamnet(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                running.set(false);
            }
            return posts;
        });
    }

    @Override
    public PostEntity crawlOneVietnamnetPost(String url) throws IOException {
        PostEntity postEntity = crawlVietnamnetPost(url);
        if (postEntity != null) {
            postRepository.save(postEntity);
        }
        return postEntity;
    }

    @Override
    public void stopCrawling() {
        running.set(false);
    }

    private List<String> crawlVietnamnetPostUrlByTag(String url, boolean isScheduled) throws IOException {
        List<String> pagePostUrlList = new ArrayList<>();
        int max = isScheduled ? 5 : Constants.Vietnamnet.MAX_PAGE;
        if (Constants.Vietnamnet.DEAD_PAGE.contains(url)) {
            return null;
        }
        for (int page = 0 ; page <= max ; page++) {
            String newUrl;
            if (url.equalsIgnoreCase("https://vietnamnet.vn/du-lich/chuyen-cua-nhung-dong-song-sk0008F1.html")) {
                newUrl = url.replace(".html", String.format(Constants.Vietnamnet.PAGE, page) + ".html");
            } else {
                newUrl = url + String.format(Constants.Vietnamnet.PAGE, page);
            }
            System.out.println(newUrl);
            try {
                Document document = Jsoup.connect(newUrl).get();
                Elements highlightPosts = document.getElementsByClass(Constants.Vietnamnet.VERTICAL_POST_CLASS);
                for (Element highlightPost : highlightPosts) {
                    String postUrl = highlightPost.getElementsByTag(Constants.Tag.A).first().absUrl(Constants.Attribute.HREF);
                    System.out.println(postUrl);
                    pagePostUrlList.add(postUrl);
                }
                Elements regularPosts = document.getElementsByClass(Constants.Vietnamnet.REGULAR_POST_CLASS);
                if (regularPosts.isEmpty()) break;
                for (Element regularPost : regularPosts) {
                    String postUrl = regularPost.getElementsByTag(Constants.Tag.A).first().absUrl(Constants.Attribute.HREF);
                    System.out.println(postUrl);
                    pagePostUrlList.add(postUrl);
                }
            } catch (UnsupportedMimeTypeException e) {
                System.out.println("Unsupported Type!");
            }
        }
        return pagePostUrlList;
    }

    private PostEntity crawlVietnamnetPost(String url) throws IOException {
        List<String> tagList = new ArrayList<>();
        Document document;
        try {
            document = Jsoup.connect(url).get();
            Elements scripts = document.getElementsByTag(Constants.Tag.SCRIPT);
            String scriptContent = "";
            for (Element script : scripts) {
                String scriptText = script.html();
                if (scriptText.contains(Constants.Vietnamnet.ARTICLE_MAIN_CATEGORY)) {
                    scriptContent = scriptText;
                    break;
                }
            }
            String articleCategory = extractValue(scriptContent, Constants.Vietnamnet.ARTICLE_MAIN_CATEGORY);
            if (articleCategory != null) tagList.add(articleCategory);
            String articleSubCategory = extractValue(scriptContent, Constants.Vietnamnet.ARTICLE_SUB_CATEGORY);
            if (articleSubCategory != null) tagList.add(articleSubCategory);
            String author = extractValue(scriptContent, Constants.Vietnamnet.ARTICLE_AUTHOR);
            LocalDateTime time = convertToLocalDateTime(extractValue(scriptContent, Constants.Vietnamnet.ARTICLE_PUBLISH_DATE));
            String articleType = extractValue(scriptContent, Constants.Vietnamnet.ARTICLE_TYPE);
//            String id = url.substring(url.lastIndexOf("/") + 1).replace(".html", "");
            String title = document.getElementsByTag(Constants.Tag.TITLE).first().text();
            String summary = null;
            String content = null;
            if (Constants.Vietnamnet.REGULAR_POST.equalsIgnoreCase(articleType)
                    || Constants.Vietnamnet.MULTI_PAGE_POST.equalsIgnoreCase(articleType)
                    || Constants.Vietnamnet.IMAGE_POST.equalsIgnoreCase(articleType)
            ) {
                summary = document.getElementsByClass(Constants.Vietnamnet.SUMMARY_CLASS).text();
                content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_CLASS).text();
                if (content == null || "".equals(content)) {
                    content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_CLASS_V2).text();
                }
            } else if (Constants.Vietnamnet.EMANGAZINE_POST.equalsIgnoreCase(articleType)){
                content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_EMAGAZINE_CLASS).text();
                Element metaTag = document.select("meta[property=og:description]").first();
                if (metaTag != null) {
                    summary = metaTag.attr("content");
                }
                if (content == null || "".equals(content)) {
                    Elements div = document.getElementsByTag(Constants.Tag.DIV);
                    for (Element divElement : div) {
                        if (Objects.equals(divElement.className(), Constants.Vietnamnet.CONTENT_BIOGRAPHY_CLASS)) {
                            content = divElement.text();
                            break;
                        }
                    }
                }
            } else if (Constants.Vietnamnet.LIVE_POST.equalsIgnoreCase(articleType)){
                content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_LIVE_CLASS).text();
                summary = document.getElementsByClass(Constants.Vietnamnet.SUMMARY_CLASS).text();
            } else if (Constants.Vietnamnet.INFOGRAPHIC_POST.equalsIgnoreCase(articleType)) {
                summary = document.getElementsByClass((Constants.Vietnamnet.SUMMARY_INFOGRAPHIC_CLASS)).text();
                content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_CLASS).text();
            } else if (Constants.Vietnamnet.VIDEO_POST.equalsIgnoreCase(articleType)) {
                summary = document.getElementsByClass((Constants.Vietnamnet.SUMMARY_VIDEO_CLASS)).text();
                content = document.getElementsByClass(Constants.Vietnamnet.CONTENT_VIDEO_CLASS).text();
            } else if (articleType == null || "".equals(articleType) || Constants.Vietnamnet.DEAD_POST.contains(articleType)) {
                return null;
            }
            return PostEntity.builder()
                    .url(url)
                    .title(title)
                    .author(author)
                    .time(time)
                    .tagList(tagList)
                    .summary(summary)
                    .content(content)
                    .build();
        } catch (HttpStatusException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String extractValue(String scriptContent, String fieldName) {
        int startIndex = scriptContent.indexOf(fieldName);
        if (startIndex == -1) {
            return null;
        }
        char quoteType = '\'';
        startIndex = scriptContent.indexOf(quoteType, startIndex + fieldName.length()) + 4;
        int endIndex = scriptContent.indexOf(quoteType, startIndex);
        if (startIndex < endIndex) {
            return scriptContent.substring(startIndex, endIndex);
        }
        return null;
    }

    private static LocalDateTime convertToLocalDateTime(String time) {
        if (time == null) {
            return null;
        }
        time = time.replace(" +", "+");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(time, formatter);
        return offsetDateTime.toLocalDateTime();
    }
}
