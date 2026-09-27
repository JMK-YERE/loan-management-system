package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
