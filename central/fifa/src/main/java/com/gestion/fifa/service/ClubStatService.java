// ClubStatsService.java
package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.ClubCrudOperation;
import com.gestion.fifa.endpoint.rest.ClubRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubStatService {

    private final ClubCrudOperation repository;


    public List<ClubRanking> getBestClubs(int top) {
        return repository.findTopClubs(top);
    }
}
