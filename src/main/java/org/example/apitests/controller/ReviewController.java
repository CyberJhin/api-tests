package org.example.apitests.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.apitests.model.DTO.ReviewDTO;
import org.example.apitests.model.Game;
import org.example.apitests.model.Review;
import org.example.apitests.model.mapper.ReviewMapper;
import org.example.apitests.service.GameService;
import org.example.apitests.service.ReviewService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final GameService gameService;

    public ReviewController(ReviewService reviewService, GameService gameService) {
        this.reviewService = reviewService;
        this.gameService = gameService;
    }

    @QueryMapping
    public ReviewDTO reviewById(@Argument Long id) {
        return reviewService.getById(id).map(ReviewMapper::toDTO).orElse(null);
    }

    @QueryMapping
    public ReviewPageDTO reviewsByGame(@Argument Long gameId, @Argument int page, @Argument int size) {
        var pageResult = reviewService.getByGameId(gameId, page, size);
        List<ReviewDTO> content = pageResult.getContent().stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
        return new ReviewPageDTO(content, pageResult.getNumber(), pageResult.getTotalPages(), (int) pageResult.getTotalElements());
    }

    @MutationMapping
    public ReviewDTO createReview(@Argument("input") ReviewInput input) {
        Game game = gameService.getById(Long.parseLong(input.getGameId())).orElseThrow();
        Review review = Review.builder()
                .author(input.getAuthor())
                .content(input.getContent())
                .rating(input.getRating())
                .game(game)
                .build();
        return ReviewMapper.toDTO(reviewService.save(review));
    }

    @MutationMapping
    public ReviewDTO updateReview(@Argument Long id, @Argument("input") ReviewInput input) {
        Review review = reviewService.getById(id).orElseThrow();
        Game game = gameService.getById(Long.parseLong(input.getGameId())).orElseThrow();
        review.setAuthor(input.getAuthor());
        review.setContent(input.getContent());
        review.setRating(input.getRating());
        review.setGame(game);
        return ReviewMapper.toDTO(reviewService.save(review));
    }

    @MutationMapping
    public Boolean deleteReview(@Argument Long id) {
        reviewService.delete(id);
        return true;
    }

    @Getter
    @Setter
    public static class ReviewInput {
        // getters and setters
        private String author;
        private String content;
        private Integer rating;
        private String gameId;

    }

    @Getter
    public static class ReviewPageDTO {
        // getters
        private List<ReviewDTO> content;
        private int pageNumber;
        private int totalPages;
        private int totalElements;

        public ReviewPageDTO(List<ReviewDTO> content, int pageNumber, int totalPages, int totalElements) {
            this.content = content;
            this.pageNumber = pageNumber;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }

    }
}
