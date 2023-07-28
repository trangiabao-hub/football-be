package com.footballbe.object;

import com.footballbe.dto.MatchData;
import lombok.Data;

import java.util.List;

@Data
public class Match {
    private String id;
    private String season_id;
    private String competition_id;
    private String home_team_id;
    private String away_team_id;
    private int status_id;
    private int match_time;
    private String venue_id;
    private String referee_id;
    private int neutral;
    private String note;
    private List<Integer> home_scores;
    private List<Integer> away_scores;
    private String home_position;
    private String away_position;
    private MatchData.Coverage coverage;
    private MatchData.Round round;
    private String related_id;
    private List<Integer> agg_score;
    private MatchData.Environment environment;
    private int updated_at;

    Team homeTeam;
    Team awayTeam;

    Competition competition;
    List<TimeWithStatistic> statistics;
}
