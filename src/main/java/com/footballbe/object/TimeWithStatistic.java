package com.footballbe.object;

import com.footballbe.enums.TimeType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TimeWithStatistic {
    long time;

    List<Statistic> statistics;

    TimeType type;
}
