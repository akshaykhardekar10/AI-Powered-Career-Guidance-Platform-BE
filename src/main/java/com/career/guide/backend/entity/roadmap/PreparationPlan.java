package com.career.guide.backend.entity.roadmap;

import com.career.guide.backend.dto.roadmap.WeeklyPreparationWeek;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.converter.WeeklyPreparationWeekListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "preparation_plans", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"})
})
public class PreparationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roadmap_id")
    private PersonalizedRoadmap roadmap;

    // Raw AI output (may include text around JSON)
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // Parsed weeks array persisted to avoid recomputation
    @Column(name = "weeks_array", columnDefinition = "TEXT")
    @Convert(converter = WeeklyPreparationWeekListConverter.class)
    private List<WeeklyPreparationWeek> weeksArray;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
