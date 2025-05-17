package org.example.apitests.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GameDTO {
    private Long id;
    private String title;
    private String genre;
    private Long studioId;
    private String studioName;
    private List<ReviewDTO> reviews;
}
