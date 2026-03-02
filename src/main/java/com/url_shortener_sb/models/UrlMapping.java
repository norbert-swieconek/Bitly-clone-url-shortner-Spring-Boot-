package com.url_shortener_sb.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount = 0;
    private LocalDateTime createdDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "urlMapping")
    private List<ClickEvent> clickEvents;

    public UrlMapping() {}

    public UrlMapping(String originalUrl, String shortUrl, LocalDateTime createdDate) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdDate = createdDate;
        this.clickEvents = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public String getShortUrl() {
        return this.shortUrl;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<ClickEvent> getClickEvents() {
        return this.clickEvents;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}
