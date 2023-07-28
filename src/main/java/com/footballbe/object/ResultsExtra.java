package com.footballbe.object;

import com.footballbe.dto.MatchData;
import lombok.Data;

import java.util.List;

@Data
public class ResultsExtra {
    private List<Competition> competition;
    private List<Team> team;
}
