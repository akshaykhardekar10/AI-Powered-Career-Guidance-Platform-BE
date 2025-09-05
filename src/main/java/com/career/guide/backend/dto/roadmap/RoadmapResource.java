package com.career.guide.backend.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoadmapResource {
    private String type;   // course|docs|video|tool|book
    private String title;
    private String url;
    private Boolean free;
}
