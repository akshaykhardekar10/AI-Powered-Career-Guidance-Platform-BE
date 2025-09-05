package com.career.guide.backend.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoadmapMilestone {
    private String title;
    @JsonProperty("sub title")
    private String subTitle;
    private String description;
    private Integer durationWeeks;
    private java.util.List<String> prerequisites;
    private java.util.List<RoadmapResource> resources;
    private java.util.List<String> dependencies;
    private java.util.List<String> tags;
    private String documentationLink;
    private Boolean completed;
}
