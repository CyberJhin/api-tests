package org.example.apitests.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class StudioDTO {
    private Long id;
    private String name;
    private String country;
    private List<Long> gameIds;
}
