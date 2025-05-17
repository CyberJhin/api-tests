package org.example.apitests.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.apitests.model.DTO.GameDTO;
import org.example.apitests.model.Game;
import org.example.apitests.model.Studio;
import org.example.apitests.model.mapper.GameMapper;
import org.example.apitests.service.GameService;
import org.example.apitests.service.StudioService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GameController {
    private final GameService gameService;
    private final StudioService studioService;

    public GameController(GameService gameService, StudioService studioService) {
        this.gameService = gameService;
        this.studioService = studioService;
    }

    @QueryMapping
    public GameDTO gameById(@Argument Long id) {
        return gameService.getById(id).map(GameMapper::toDTO).orElse(null);
    }

    @QueryMapping
    public List<GameDTO> gamesByTitle(@Argument String title) {
        return gameService.getByTitle(title).stream().map(GameMapper::toDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public List<GameDTO> gamesByTitlePart(@Argument String titlePart) {
        return gameService.searchByTitlePart(titlePart).stream().map(GameMapper::toDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public List<GameDTO> allGames() {
        return gameService.getAll().stream().map(GameMapper::toDTO).collect(Collectors.toList());
    }

    @MutationMapping
    public GameDTO createGame(@Argument("input") GameInput input) {
        Studio studio = studioService.getById(Long.parseLong(input.getStudioId())).orElseThrow();
        Game game = Game.builder()
                .title(input.getTitle())
                .genre(input.getGenre())
                .studio(studio)
                .build();
        return GameMapper.toDTO(gameService.save(game));
    }

    @MutationMapping
    public GameDTO updateGame(@Argument Long id, @Argument("input") GameInput input) {
        Game game = gameService.getById(id).orElseThrow();
        Studio studio = studioService.getById(Long.parseLong(input.getStudioId())).orElseThrow();
        game.setTitle(input.getTitle());
        game.setGenre(input.getGenre());
        game.setStudio(studio);
        return GameMapper.toDTO(gameService.save(game));
    }

    @MutationMapping
    public Boolean deleteGame(@Argument Long id) {
        gameService.delete(id);
        return true;
    }

    // Inner static class for GraphQL input mapping
    @Setter
    @Getter
    public static class GameInput {
        // getters and setters
        private String title;
        private String genre;
        private String studioId;

    }
}