package org.example.apitests.model.mapper;

import org.example.apitests.model.DTO.StudioDTO;
import org.example.apitests.model.Game;
import org.example.apitests.model.Studio;

import java.util.stream.Collectors;

public class StudioMapper {
    public static StudioDTO toDTO(Studio studio) {
        StudioDTO dto = new StudioDTO();
        dto.setId(studio.getId());
        dto.setName(studio.getName());
        dto.setCountry(studio.getCountry());
        if (studio.getGames() != null) {
            dto.setGameIds(studio.getGames().stream().map(Game::getId).collect(Collectors.toList()));
        }
        return dto;
    }
}