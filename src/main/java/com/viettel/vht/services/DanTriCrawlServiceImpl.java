package com.viettel.vht.services;

import com.mongodb.client.model.Filters;
import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.repositories.PostRepository;
import com.viettel.vht.services.interfaces.DanTriCrawlService;
import com.viettel.vht.utils.Constants;
import com.viettel.vht.utils.Utils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DanTriCrawlServiceImpl implements DanTriCrawlService {
    @Autowired
    private PostRepository postRepository;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    @Override
    public List<PostEntity> crawlDanTri(boolean isScheduled) throws IOException {
        List<PostEntity> postEntityList = new ArrayList<>();
        if (isScheduled && !running.get()) {
            return postEntityList;
        }
        Document document = Jsoup.connect(Constants.DanTri.BASE_URL).get();
        Elements li = document.getElementsByClass(Constants.DanTri.LI_CLASS);
        for (Element element : li) {
            Elements liList = element
                    .getElementsByTag(Constants.Tag.OL)
                    .first()
                    .getElementsByTag(Constants.Tag.LI);
            for (Element liElem : liList) {
                if (isScheduled && !running.get()) {
                    return postEntityList;
                }
                String pageUrl = liElem.getElementsByTag(Constants.Tag.A).first().absUrl(Constants.Attribute.HREF);
                System.out.println(pageUrl);
                if (pageUrl.startsWith(Constants.DanTri.BASE_URL)
                        && !pageUrl.contains("collection")
                        && !pageUrl.contains("https://dantri.com.vn/kinh-doanh/esg-phat-trien-ben-vung")
                        && !pageUrl.contains("https://dantri.com.vn/tam-long-nhan-ai")
                        && !pageUrl.contains("https://dantri.com.vn/the-thao/lich-thi-dau.htm")
                ) {
                    List<String> pagePostUrlList = crawlDanTriPostUrlByTag(pageUrl, isScheduled);
                    if (!pagePostUrlList.isEmpty()) {
                        for (String postUrl : pagePostUrlList) {
                            if (isScheduled && !running.get()) {
                                return postEntityList;
                            }
                            System.out.println(postUrl);
                            org.bson.Document doc = postRepository.postCollection.find(Filters.eq("url", postUrl)).first();
                            if (doc == null && postUrl != null && !"".equals(postUrl)) {
                                PostEntity postEntity = crawlDanTriPost(postUrl, null);
                                if (postEntity != null /* && postEntity.getContent() != null && !"".equals(postEntity.getContent())*/) {
                                    postEntity.setSource(Constants.DanTri.SOURCE_NAME);
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
    public CompletableFuture<List<PostEntity>> crawlDanTriAsync() throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            running.set(true);
            List<PostEntity> posts = new ArrayList<>();
            try {
                posts = crawlDanTri(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                running.set(false);
            }
            return posts;
        });
    }

    @Override
    public PostEntity crawlOneDanTriPost(String url) throws IOException {
        PostEntity postEntity = crawlDanTriPost(url, null);
        if (postEntity != null) {
            postRepository.save(postEntity);
        }
        return postEntity;
    }

    @Override
    public void stopCrawling() {
        running.set(false);
    }

    private List<String> crawlDanTriPostUrlByTag(String url, boolean isScheduled) throws IOException {
        List<String> pagePostUrlList = new ArrayList<>();
        int max = isScheduled ? 5 : Constants.DanTri.MAX_PAGE;
        for (int page = 1 ; page <= max; page++) {
            String newUrl = url.replace(".htm", String.format(Constants.DanTri.PAGE, page));
            System.out.println(newUrl);
            Document document = Jsoup.connect(newUrl).get();
            Elements articleList = document.getElementsByClass(Constants.DanTri.ARTICLE_CLASS);
            for (Element article : articleList) {
                String articleUrl = article.absUrl(Constants.Attribute.DATA_CONTENT_TARGET);
                System.out.println(articleUrl);
                pagePostUrlList.add(articleUrl);
            }
        }
        return pagePostUrlList;
    }

    private PostEntity crawlDanTriPost(String url, Document document) throws IOException {
        List<String> tagList = new ArrayList<>();
        try {
            if (document == null) {
                document = Jsoup.connect(url).get();
            }
            String title = document.getElementsByTag(Constants.Tag.TITLE).first().text();
//            String id = url.substring(url.lastIndexOf("/") + 1).replace(".htm", "");
            if (document.getElementsByClass(Constants.DanTri.TAG_CLASS) != null) {
                Elements tagListElem = document.getElementsByClass(Constants.DanTri.TAG_CLASS);
                for (Element a : tagListElem) {
                    String tag = a.attr(Constants.Attribute.TITLE);
                    if (!"".equalsIgnoreCase(tag)) tagList.add(tag);
                }
            }
            if (document.getElementsByClass(Constants.DanTri.TAG_DNEWS_CLASS) != null) {
                String tag = document.getElementsByClass(Constants.DanTri.TAG_DNEWS_CLASS).text();
                if (!"".equalsIgnoreCase(tag)) tagList.add(tag);
            }
            if (document.getElementsByClass(Constants.DanTri.TAG_DBIZ_CLASS).first() != null) {
                Elements tags = document.getElementsByClass(Constants.DanTri.TAG_DBIZ_CLASS).first().getElementsByTag(Constants.Tag.A);
                for (Element tagElem : tags) {
                    String tag = tagElem.attr(Constants.Attribute.TITLE);
                    if (!"".equalsIgnoreCase(tag)) tagList.add(tag);
                }
            }
            Element authorElem = Utils.coalesce(
                    document.getElementsByClass(Constants.DanTri.AUTHOR_NAME_DBIZ_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.AUTHOR_NAME_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.AUTHOR_NAME_PHOTO_STORY_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.AUTHOR_NAME_DNEWS_CLASS).first()
            );
            String author = null;
            if (authorElem == null) {
                Elements pList = document.select("p[style=text-align:right]");
                for (Element p : pList) {
                    Element nextElem = p.nextElementSibling();
                    if (nextElem != null && nextElem.tagName().equals("time")) {
                        author = p.text();
                    }
                }
            } else {
                author = authorElem.text();
            }
            Element timeElem = Utils.coalesce(
                    document.getElementsByClass(Constants.DanTri.TIME_DBIZ_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.TIME_PHOTO_STORY_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.TIME_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.TIME_DNEWS_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.TIME_DMAGAZINE_CLASS).first()
            );
            LocalDateTime time = null;
            if (timeElem != null) {
                time = LocalDateTime.parse(timeElem.attr(Constants.Attribute.DATE_TIME), Constants.DanTri.formatter);
            }
            String content = Utils.coalesce(
                    document.getElementsByClass(Constants.DanTri.CONTENT_DBIZ_CLASS).text(),
                    document.getElementsByClass(Constants.DanTri.CONTENT_CLASS).text(),
                    document.getElementsByClass(Constants.DanTri.CONTENT_PHOTO_STORY_CLASS).text(),
                    document.getElementsByClass(Constants.DanTri.CONTENT_DNEWS_CLASS).text(),
                    document.getElementsByClass(Constants.DanTri.CONTENT_DMAGAZINE_CLASS).text()
            );
//            if (content == null) {
//                content = crawlReportPost(url);
//            }
            String summary = null;
            Element summaryElem = Utils.coalesce(
                    document.getElementsByClass(Constants.DanTri.SUMMARY_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.SUMMARY_PHOTO_STORY_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.SUMMARY_DNEWS_CLASS).first(),
                    document.getElementsByClass(Constants.DanTri.SUMMARY_DMAGAZINE_CLASS).first()
            );
            if (summaryElem != null) {
                summary = summaryElem.text();
                summary = summary.replace("(Dân trí)", "");
                summary = summary.replaceFirst(" - ", "");
            } else {
                Element metaTag = document.select("meta[property=og:description]").first();
                if (metaTag != null) {
                    summary = metaTag.attr("content");
                }
            }
            return PostEntity.builder()
                    .url(url)
                    .title(title)
                    .author(author)
                    .time(time)
                    .tagList(tagList)
                    .summary(summary.replace("(Dân trí)", ""))
                    .content(content)
                    .build();
        } catch (DateTimeParseException e) {
            return null;
        } catch (HttpStatusException e) {
            return null;
        }
    }

    private String crawlReportPost(String url) throws IOException {
        String content = null;
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String pageSource = driver.getPageSource();
        Document document = Jsoup.parse(pageSource);
        Elements iframes = document.getElementsByTag("iframe");
        for (Element iframe : iframes) {
            if (iframe.attr("src").contains("https://gadgets.dantri.com.vn/reports")) {
                Document document1 = Jsoup.connect(iframe.attr("src")).get();
                System.out.println(document1);
                System.out.println(document1.getElementsByClass(document.getElementsByClass(Constants.DanTri.CONTENT_REPORT_CLASS).text()));
                content =document1.getElementsByClass(Constants.DanTri.CONTENT_REPORT_CLASS).text();
            }
        }
        driver.quit();
        return content;
    }
}
