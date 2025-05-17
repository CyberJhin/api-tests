package org.example.apitests.service.graphql;

import org.example.apitests.model.Game;
import org.example.apitests.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> getById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> getByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public List<Game> searchByTitlePart(String titlePart) {
        return gameRepository.findByTitleContainingIgnoreCase(titlePart);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}
