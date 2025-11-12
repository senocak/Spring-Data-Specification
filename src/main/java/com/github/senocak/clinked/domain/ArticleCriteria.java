package com.github.senocak.clinked.domain;

public class ArticleCriteria {
    private String title;
    private String author;
    private String slug;
    private String publishFrom;
    private String publishTo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPublishFrom() {
        return publishFrom;
    }

    public void setPublishFrom(String publishFrom) {
        this.publishFrom = publishFrom;
    }

    public String getPublishTo() {
        return publishTo;
    }

    public void setPublishTo(String publishTo) {
        this.publishTo = publishTo;
    }
}
