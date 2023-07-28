package com.footballbe.dto;

import lombok.Data;

import java.util.List;

@Data
public class RealTime {
    private int code;
    private List<StatisticalResult> results;
}
