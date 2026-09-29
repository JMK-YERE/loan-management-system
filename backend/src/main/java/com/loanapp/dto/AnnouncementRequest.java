package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnnouncementRequest {

    @NotBlank(message = "Kichwa kinahitajika")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Maudhui yanahitajika")
    private String content;

    @Size(max = 50)
    private String tag;

    @Size(max = 100)
    private String color;

    @Size(max = 20)
    private String emoji;

    @Size(max = 100)
    private String ctaText;

    @Size(max = 200)
    private String ctaLink;

    private Boolean active;

    private Integer displayOrder;

    public AnnouncementRequest() {
    }

    public AnnouncementRequest(String title, String content, String tag, String color,
                               String emoji, String ctaText, String ctaLink,
                               Boolean active, Integer displayOrder) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.color = color;
        this.emoji = emoji;
        this.ctaText = ctaText;
        this.ctaLink = ctaLink;
        this.active = active;
        this.displayOrder = displayOrder;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getCtaText() { return ctaText; }
    public void setCtaText(String ctaText) { this.ctaText = ctaText; }

    public String getCtaLink() { return ctaLink; }
    public void setCtaLink(String ctaLink) { this.ctaLink = ctaLink; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
