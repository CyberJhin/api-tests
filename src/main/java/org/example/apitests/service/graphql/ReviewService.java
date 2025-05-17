package org.example.apitests.service.graphql;

import org.example.apitests.model.Review;
import org.example.apitests.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Page<Review> getByGameId(Long gameId, int page, int size) {
        return reviewRepository.findByGameId(gameId, PageRequest.of(page, size));
    }

    public Optional<Review> getById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}