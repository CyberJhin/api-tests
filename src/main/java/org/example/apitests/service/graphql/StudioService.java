package org.example.apitests.service.graphql;

import org.example.apitests.model.Studio;
import org.example.apitests.repository.StudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudioService {
    private final StudioRepository studioRepository;

    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public List<Studio> getAll() {
        return studioRepository.findAll();
    }

    public Optional<Studio> getById(Long id) {
        return studioRepository.findById(id);
    }

    public List<Studio> searchByNamePart(String namePart) {
        return studioRepository.findByNameContainingIgnoreCase(namePart);
    }

    public Studio save(Studio studio) {
        return studioRepository.save(studio);
    }

    public void delete(Long id) {
        studioRepository.deleteById(id);
    }
}
