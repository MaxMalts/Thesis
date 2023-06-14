package com.thesis.apiserver.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record Area(
    @PositiveOrZero int x1,
    @PositiveOrZero int y1,
    @PositiveOrZero int x2,
    @PositiveOrZero int y2
) {}
