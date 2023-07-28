package com.footballbe.dto;
import lombok.Data;
import java.util.List;

@Data
public class MatchData {
    private int code;
    private Query query;
    private List<Result> results;
    private ResultsExtra results_extra;

    @Data
    public static class Query {
        private String Inquiry;
        private int total;
        private String type;
        private String date;
    }

    @Data
    public static class Result {
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
        private Coverage coverage;
        private Round round;
        private String related_id;
        private List<Integer> agg_score;
        private Environment environment;
        private int updated_at;
        private TeamData homeTeam;
        private TeamData awayTeam;

        private CompetitionData competition;

        private List<Statistical.MatchStatistics> statistical;



    }

    @Data
    public static class Coverage {
        private int animation;
        private int lineup;
    }

    @Data
    public static class Round {
        private String stage_id;
        private int group_num;
        private int round_num;
    }

    @Data
    public static class Environment {
        private int weather;
        private String pressure;
        private String temperature;
        private String wind;
        private String humidity;
    }

    @Data
    public static class ResultsExtra {
        private List<CompetitionData> competition;
        private List<TeamData> team;
        private List<RefereeData> referee;
        private List<VenueData> venue;
        private List<SeasonData> season;
        private List<StageData> stage;
    }

    @Data
    public static class CompetitionData {
        private String id;
        private String name;
        private String logo;
    }

    @Data
    public static class TeamData {
        private String id;
        private String name;
        private String logo;
    }

    @Data
    public static class RefereeData {
        private String id;
        private String name;
        private String logo;
    }

    @Data
    public static class VenueData {
        private String id;
        private String name;
    }

    @Data
    public static class SeasonData {
        private String id;
        private String competition_id;
        private String year;
    }

    @Data
    public static class StageData {
        private String id;
        private String season_id;
        private String name;
        private int mode;
        private int group_count;
        private int round_count;
        private int order;
    }

    // Method to map TeamData in ResultsExtra to home_team_id and away_team_id in Result
    public void mapTeams() {
        if (results != null && results_extra != null) {
            for (Result result : results) {
                mapTeam(result, results_extra);
            }
        }
    }

    private void mapTeam(Result result, ResultsExtra resultsExtra) {
        if (result != null && resultsExtra != null) {
            for (TeamData teamData : resultsExtra.getTeam()) {
                if (teamData.getId().equals(result.getHome_team_id())) {
                    result.setHomeTeam(teamData);
                } else if (teamData.getId().equals(result.getAway_team_id())) {
                    result.setAwayTeam(teamData);
                }
            }
        }
    }

    // Method to map CompetitionData in ResultsExtra to competition in Result
    public void mapCompetition() {
        if (results != null && results_extra != null) {
            for (Result result : results) {
                mapCompetition(result, results_extra);
            }
        }
    }

    private void mapCompetition(Result result, ResultsExtra resultsExtra) {
        if (result != null && resultsExtra != null) {
            for (CompetitionData competitionData : resultsExtra.getCompetition()) {
                if (competitionData.getId().equals(result.getCompetition_id())) {
                    result.setCompetition(competitionData);
                    break; // Assuming competition_id is unique, no need to continue searching
                }
            }
        }
    }
}
