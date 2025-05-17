package org.example.apitests.repository;

import org.example.apitests.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContainingIgnoreCase(String titlePart);
    List<Game> findByTitle(String title);
}
