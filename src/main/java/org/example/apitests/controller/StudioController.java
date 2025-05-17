package org.example.apitests.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.apitests.model.DTO.StudioDTO;
import org.example.apitests.model.Studio;
import org.example.apitests.model.mapper.StudioMapper;
import org.example.apitests.service.StudioService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudioController {
    private final StudioService studioService;

    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @QueryMapping
    public StudioDTO studioById(@Argument Long id) {
        return studioService.getById(id).map(StudioMapper::toDTO).orElse(null);
    }

    @QueryMapping
    public List<StudioDTO> studiosByNamePart(@Argument String namePart) {
        return studioService.searchByNamePart(namePart).stream().map(StudioMapper::toDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public List<StudioDTO> allStudios() {
        return studioService.getAll().stream().map(StudioMapper::toDTO).collect(Collectors.toList());
    }

    @MutationMapping
    public StudioDTO createStudio(@Argument("input") StudioInput input) {
        Studio studio = Studio.builder()
                .name(input.getName())
                .country(input.getCountry())
                .build();
        return StudioMapper.toDTO(studioService.save(studio));
    }

    @MutationMapping
    public StudioDTO updateStudio(@Argument Long id, @Argument("input") StudioInput input) {
        Studio studio = studioService.getById(id).orElseThrow();
        studio.setName(input.getName());
        studio.setCountry(input.getCountry());
        return StudioMapper.toDTO(studioService.save(studio));
    }

    @MutationMapping
    public Boolean deleteStudio(@Argument Long id) {
        studioService.delete(id);
        return true;
    }

    @Setter
    @Getter
    public static class StudioInput {
        private String name;
        private String country;

    }
}