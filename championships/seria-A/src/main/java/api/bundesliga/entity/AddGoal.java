package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddGoal {
    private String clubId;
    private String scorerIdentifier;
    private Integer minuteOfGoal;
}
