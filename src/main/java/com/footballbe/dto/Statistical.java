package com.footballbe.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Statistical {
    private int code;
    private StatisticalResult results;

    @Data
    public static class StatisticalResult {
        private String id;
        private List<Object> score;
        private List<MatchStatistics> stats;
        private List<MatchIncident> incidents;
    }

    @Data
    public static class MatchStatistics {
        private StatisticsType type;
        private int home;
        private int away;

        // Getters and Setters
    }

    @Data
    public static class MatchIncident {
        private int type;
        private int position;
        private int time;
        private String player_id;
        private String player_name;
        private String assist1_id;
        private String assist1_name;
        private String assist2_id;
        private String assist2_name;
        private int home_score;
        private int away_score;
        private String in_player_id;
        private String in_player_name;
        private String out_player_id;
        private String out_player_name;
        private VarReason var_reason;
        private VarResult var_result;
        private int reason_type;

        // Getters and Setters
    }

    // Enums
    public enum StatisticsType {
        NONE,
        GOAL,
        CORNER,
        YELLOW_CARD,
        RED_CARD,
        OFFSIDE,
        FREE_KICK,
        GOAL_KICK,
        PENALTY,
        SUBSTITUTION,
        START,
        MIDFIELD,
        END,
        HALFTIME_SCORE,
        NONE2,
        CARD_UPGRADE_CONFIRMED,
        PENALTY_MISSED,
        OWN_GOAL,
        NONE3,
        INJURY_TIME,
        NONE4,
        SHOTS_ON_TARGET,
        SHOTS_OFF_TARGET,
        ATTACKS,
        DANGEROUS_ATTACK,
        BALL_POSSESSION,
        OVERTIME_IS_OVER,
        PENALTY_KICK_ENDED,
        VAR,
        PENALTY_SHOOTOUT,
        PENALTY_MISSED_PENALTY_SHOOTOUT;
    }

//    public enum IncidentType {
//        // Define your incident types here
//        // For example:
//        GOAL,
//        RED_CARD,
//        YELLOW_CARD,
//        SUBSTITUTION,
//        VAR
//    }

    public enum PositionType {
        NEUTRAL,
        HOME_TEAM,
        AWAY_TEAM
    }

    public enum VarReason {
        GOAL_AWARDED,
        GOAL_NOT_AWARDED,
        PENALTY_AWARDED,
        PENALTY_NOT_AWARDED,
        RED_CARD_GIVEN,
        CARD_UPGRADE,
        MISTAKEN_IDENTITY,
        OTHER
    }

    public enum VarResult {
        GOAL_CONFIRMED,
        GOAL_CANCELLED,
        PENALTY_CONFIRMED,
        PENALTY_CANCELLED,
        RED_CARD_CONFIRMED,
        RED_CARD_CANCELLED,
        CARD_UPGRADE_CONFIRMED,
        CARD_UPGRADE_CANCELLED,
        ORIGINAL_DECISION,
        ORIGINAL_DECISION_CHANGED,
        UNKNOWN
    }
}
