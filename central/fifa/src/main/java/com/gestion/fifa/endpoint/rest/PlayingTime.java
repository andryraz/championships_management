package com.gestion.fifa.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayingTime {
    private long value;
    private DurationUnit durationUnit;
}