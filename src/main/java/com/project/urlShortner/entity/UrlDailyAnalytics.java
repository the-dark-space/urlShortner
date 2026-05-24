package com.project.urlShortner.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "url_daily_analytics",

        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "short_code",
                                "date"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDailyAnalytics {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "short_code")
    private String shortCode;

    private LocalDate date;

    private Long clicks;
}