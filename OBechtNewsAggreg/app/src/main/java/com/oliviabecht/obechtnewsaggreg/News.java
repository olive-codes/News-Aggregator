package com.oliviabecht.obechtnewsaggreg;

public class News {

    private final String title;
    private final String body;
    private final String url;
    private final String urlToImage;
    private final String publishedAt;

    public News (String title, String body, String url, String urlToImage, String publishedAt) {
        this.title = title;
        this.body = body;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }


}
