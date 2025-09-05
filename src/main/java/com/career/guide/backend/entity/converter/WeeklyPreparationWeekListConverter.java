package com.career.guide.backend.entity.converter;

import com.career.guide.backend.dto.roadmap.WeeklyPreparationWeek;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter(autoApply = false)
public class WeeklyPreparationWeekListConverter implements AttributeConverter<List<WeeklyPreparationWeek>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<WeeklyPreparationWeek> attribute) {
        try {
            return attribute == null ? "[]" : mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<WeeklyPreparationWeek> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) return Collections.emptyList();
            return mapper.readValue(dbData, new TypeReference<List<WeeklyPreparationWeek>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
