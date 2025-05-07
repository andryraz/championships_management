package api.bundesliga.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalInput {
    private String clubId;
    private String scorerIdentifier;
    private Integer minuteOfGoal;
}
