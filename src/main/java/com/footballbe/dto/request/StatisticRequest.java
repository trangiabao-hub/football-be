package com.footballbe.dto.request;

import com.footballbe.entity.Statistic;
import lombok.Data;

import java.util.List;

@Data
public class StatisticRequest {
    List<Statistic> statistics;
}
