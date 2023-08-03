package com.footballbe.object;

import lombok.Data;

@Data
public class Handicap {
    private int changeTime;
    private String timeOfMatch;
    private float homeWin;
    private float handicap;
    private float awayWin;
    private int matchStatus;
    private int sealDisk;
    private String score;
}
