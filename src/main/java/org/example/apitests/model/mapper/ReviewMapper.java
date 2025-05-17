package org.example.apitests.model.mapper;

import org.example.apitests.model.DTO.ReviewDTO;
import org.example.apitests.model.Review;

public class ReviewMapper {
    public static ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setAuthor(review.getAuthor());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        if (review.getGame() != null) {
            dto.setGameId(review.getGame().getId());
        }
        return dto;
    }
}