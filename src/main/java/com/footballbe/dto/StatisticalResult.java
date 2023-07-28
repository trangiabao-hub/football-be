package com.footballbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatisticalResult {
    private String id;
    private List<Object> score;
    private List<Statistical.MatchStatistics> stats;
    private List<Statistical.MatchIncident> incidents;
}
