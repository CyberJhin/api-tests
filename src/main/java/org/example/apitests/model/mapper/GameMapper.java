package org.example.apitests.model.mapper;



import org.example.apitests.model.DTO.GameDTO;
import org.example.apitests.model.Game;

import java.util.stream.Collectors;

public class GameMapper {
    public static GameDTO toDTO(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setGenre(game.getGenre());
        if (game.getStudio() != null) {
            dto.setStudioId(game.getStudio().getId());
            dto.setStudioName(game.getStudio().getName());
        }
        if (game.getReviews() != null) {
            dto.setReviews(game.getReviews().stream().map(ReviewMapper::toDTO).collect(Collectors.toList()));
        }
        return dto;
    }
}
