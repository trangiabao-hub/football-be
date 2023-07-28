package com.footballbe.object;

import com.footballbe.enums.StatisticsType;
import lombok.Data;

@Data
public class Statistic {
    private StatisticsType type;
    private int home;
    private int away;
}
