package com.career.guide.backend.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyPreparationWeek {
    private int week;
    private String title;
    private List<WeeklyPreparationData> data;
}
