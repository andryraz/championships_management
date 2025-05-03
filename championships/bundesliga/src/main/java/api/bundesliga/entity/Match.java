package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Match {
    private String id;
    private MatchClub clubPlayingHome;
    private MatchClub clubPlayingAway;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus actualStatus;
    private Integer seasonYear;
}
