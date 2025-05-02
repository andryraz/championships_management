package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Club {
    private String id;
    private String name;
    private String acronym;
    private Integer year_creation;
    private String stadium;
    private String coach_name;
    private String coach_nationality;

}