package org.example.apitests.model.DTO;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String author;
    private String content;
    private Integer rating;
    private Long gameId;
}
