package com.tikitaka.scoretracker.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class FavoriteRequest {
    @NotBlank
    private String teamId;
}