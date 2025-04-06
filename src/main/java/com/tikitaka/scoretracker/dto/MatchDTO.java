package com.tikitaka.scoretracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private String id;
    private String team1;
    private String team2;
    private String score;
    private boolean isLive;
    private String league;
}