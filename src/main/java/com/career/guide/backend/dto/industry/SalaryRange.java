package com.career.guide.backend.dto.industry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRange {
    private String role;
    private Double min;
    private Double max;
    private Double median;
    private String location;
}
