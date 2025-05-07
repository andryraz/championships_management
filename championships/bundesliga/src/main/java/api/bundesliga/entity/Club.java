package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Coach coach;

}