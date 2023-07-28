package com.footballbe.object;

import lombok.Data;

import java.util.List;
@Data
public class StatisticalResponse {
    String id;
    List<Statistic> stats;
}
