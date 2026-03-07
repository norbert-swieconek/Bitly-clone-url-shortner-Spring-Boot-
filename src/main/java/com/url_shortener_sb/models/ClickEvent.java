package com.url_shortener_sb.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime clickDate;

    @ManyToOne
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;

    public ClickEvent() {

    }

    public ClickEvent(LocalDateTime clickDate) {
        this.clickDate = clickDate;
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getClickDate() {
        return this.clickDate;
    }

    public UrlMapping getUrlMapping() {
        return this.urlMapping;
    }

    public void setClickDate(LocalDateTime clickDate) {
        this.clickDate = clickDate;
    }

    public void setUrlMapping(UrlMapping urlMapping) {
        this.urlMapping = urlMapping;
    }
}
