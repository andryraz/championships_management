package com.gestion.fifa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class SynchronizationRequest {
    private List<StatClub> clubStatistics;
    private List<StatPlayer> playerStatistics;
}

