package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MatchEntity {

    private UUID id;
    private UUID clubHomeId;
    private UUID clubAwayId;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus status;
    private UUID seasonId;
}
