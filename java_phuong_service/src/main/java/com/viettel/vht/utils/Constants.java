package com.viettel.vht.utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Constants {
    public interface DanTri {
        String SOURCE_NAME = "Báo Dân Trí";
        String BASE_URL = "https://dantri.com.vn";
        String LI_CLASS = "has-child";
        String OL_CLASS = "submenu";
        String TAG_CLASS = "dt-text-[13px] dt-leading-[140%] dt-uppercase dt-text-[#565656]";
        String TAG_DBIZ_CLASS = "special-news__breadcrumb";
        String TAG_DNEWS_CLASS = "category-name";
        String ARTICLE_CLASS = "article-item";
        String TITLE_CLASS = "title-page detail";
        String TITLE_PHOTO_STORY_CLASS = "e-magazine__title";
        String TITLE_DNEWS_CLASS = "author-name";
        String TITLE_DBIZ_CLASS = "special-news__title";
        String AUTHOR_NAME_CLASS = "author-name";
        String AUTHOR_NAME_PHOTO_STORY_CLASS = "e-magazine__meta";
        String AUTHOR_NAME_DNEWS_CLASS = "author-name";
        String AUTHOR_NAME_DBIZ_CLASS = "author-name";
        String TIME_CLASS = "author-time";
        String TIME_PHOTO_STORY_CLASS = "e-magazine__meta-item";
        String TIME_DNEWS_CLASS = "author-time";
        String TIME_DBIZ_CLASS = "author-time";
        String TIME_DMAGAZINE_CLASS = "author-info_right";
        String SUMMARY_CLASS = "singular-sapo";
        String SUMMARY_PHOTO_STORY_CLASS = "e-magazine__sapo sapo-top";
        String SUMMARY_DNEWS_CLASS = "e-magazine__sapo";
        String SUMMARY_DMAGAZINE_CLASS = "e-magazine__sapo";
        String CONTENT_CLASS = "singular-content";
        String CONTENT_PHOTO_STORY_CLASS = "e-magazine__body";
        String CONTENT_DNEWS_CLASS = "e-magazine__body dnews__body";
        String CONTENT_DMAGAZINE_CLASS = "e-magazine__body";
        String CONTENT_DBIZ_CLASS = "e-magazine__body";
        String CONTENT_REPORT_CLASS = "dantri-report__item";
        String PAGE = "/trang-%s.htm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int MAX_PAGE = 2;
    }

    public interface Vietnamnet {
        String SOURCE_NAME = "Báo Vietnamnet";
        String BASE_URL = "https://vietnamnet.vn";
        String UL_CLASS = "mainNav__list swiper-wrapper";
        String VERTICAL_POST_CLASS = "verticalPost__avt ";
        String REGULAR_POST_CLASS = "horizontalPost__main-title  vnn-title title-bold";
        List<String> DEAD_PAGE = List.of(
                "https://vietnamnet.vn/giao-duc/trac-nghiem",
                "https://vietnamnet.vn/thoi-su/quoc-hoi-khoa-xv",
                "https://vietnamnet.vn/the-thao/du-lieu-bong-da",
                "https://vietnamnet.vn/giao-duc/tuyen-dung/",
                "https://vietnamnet.vn/dieu-con-mai",
                "https://vietnamnet.vn/oto-xe-may/du-lieu-xe"
        );
        String TITLE_CLASS = "content-detail-title";
        String AUTHOR_NAME_CLASS = "article-detail-author__info";
        String AUTHOR_NAME_EMAGAZINE_CLASS = "article-detail-author__info";
        String TIME_CLASS = "bread-crumb-detail__time";
        String ARTICLE_MAIN_CATEGORY = "ArticleCategory";
        String ARTICLE_SUB_CATEGORY = "ArticleSubCategory";
        String ARTICLE_AUTHOR = "ArticleAuthor";
        String ARTICLE_PUBLISH_DATE = "ArticlePublishDate";
        String ARTICLE_TYPE = "ArticleType";
        String SUMMARY_CLASS = "content-detail-sapo sm-sapo-mb-0";
        String SUMMARY_INFOGRAPHIC_CLASS = "content-detail-sapo ";
        String SUMMARY_VIDEO_CLASS = "video-detail__sabo";
        String CONTENT_CLASS = "maincontent main-content";
        String CONTENT_CLASS_V2 = "maincontent main-content content-full-image content-full-image-v1";
        String CONTENT_EMAGAZINE_CLASS = "e-tpl-v3 e-tpl-v3-text-single e-v3-tpl-content";
        String CONTENT_LIVE_CLASS = "newsFeature__listInfomation-post";
        String CONTENT_BIOGRAPHY_CLASS = "time-line";
        String CONTENT_VIDEO_CLASS = "video-detail__content maincontent main-content";
        String REGULAR_POST = "Bài thường";
        String EMANGAZINE_POST = "Bài EMagazineLongform";
        String LIVE_POST = "Bài Live";
        String MULTI_PAGE_POST = "Bài nhiều trang";
        String INFOGRAPHIC_POST = "Bài Infographic";
        String VIDEO_POST = "Bài video";
        String IMAGE_POST = "Bài ảnh";
        List<String> DEAD_POST = List.of(
                "Bài Quiz",
                "Bài EMagazineStoryScroll"
        );
        String PAGE = "-page%s";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy - HH:mm", Locale.forLanguageTag("vi"));
        DateTimeFormatter emagazineFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy | HH:mm", Locale.forLanguageTag("vi"));
        int MAX_PAGE = 2;
    }

    public interface Tag {
        String LI = "li";
        String OL = "ol";
        String UL = "ul";
        String A = "a";
        String TITLE = "title";
        String DIV = "div";
        String SCRIPT = "script";
    }
    public interface Attribute {
        String HREF = "href";
        String DATA_CONTENT_TARGET = "data-content-target";
        String DATE_TIME = "datetime";
        String TITLE = "title";
    }
}
