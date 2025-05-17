package org.example.apitests.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    private String content;

    private Integer rating; // 1-10

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}