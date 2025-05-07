// ClubController.java
package com.gestion.fifa.endpoint;

import com.gestion.fifa.dto.ClubRanking;
import com.gestion.fifa.service.ClubStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bestClubs")
public class ClubController {

    private final ClubStatService service;


    @GetMapping
    public List<ClubRanking> getBestClubs(@RequestParam(defaultValue = "5") int top) {
        return service.getBestClubs(top);
    }
}
