package com.footballbe.controller;

import com.footballbe.enums.TimeType;
import com.footballbe.object.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class DataController {
    @Value("${api.user}")
    private String apiUser;

    @Value("${api.secret}")
    private String apiSecret;

    @Value("${api.host-server}")
    private String hostServer;

    RestTemplate restTemplate = new RestTemplate();

    //    List<Match> matches = new ArrayList<>();
    HashMap<String, Match> matches = new HashMap<>();

    @GetMapping("/matches")
    public ResponseEntity getMatches() {
        List<Match> matchesResponse = new ArrayList<>();
        List<Match> matchesResponseSorted = new ArrayList<>();
        matches.forEach((id, value) -> {
            matchesResponse.add(value);
        });
        matchesResponseSorted = matchesResponse.stream()
                .sorted(Comparator.comparingLong(Match::getMatch_time))
                .collect(Collectors.toList());
        return new ResponseEntity(matchesResponseSorted, HttpStatus.OK);
    }

    @GetMapping("/fetch")
    public void fetchDataMatch() {
        String matchDiaryUrl = hostServer + "/match/diary?user=" + apiUser + "&secret=" + apiSecret + "&date=20230728";
        ParameterizedTypeReference<APIResponse<List<Match>>> responseType = new ParameterizedTypeReference<APIResponse<List<Match>>>() {
        };
        ResponseEntity<APIResponse<List<Match>>> responseEntity = restTemplate.exchange(matchDiaryUrl, HttpMethod.GET, null, responseType);
        APIResponse<List<Match>> matchResponse = responseEntity.getBody();

        HashMap<String, Competition> listCompetition = new HashMap<>();
        HashMap<String, Team> listTeam = new HashMap<>();

        matchResponse.getResults_extra().getTeam().forEach(item -> {
            listTeam.put(item.getId(), item);
        });

        matchResponse.getResults_extra().getCompetition().forEach(item -> {
            listCompetition.put(item.getId(), item);
        });

        for (Match match : matchResponse.getResults()) {
            match.setAwayTeam(listTeam.get(match.getAway_team_id()));
            match.setHomeTeam(listTeam.get(match.getHome_team_id()));
            match.setCompetition(listCompetition.get(match.getCompetition_id()));
            matches.put(match.getId(), match);
        }
    }

    public ResponseEntity fetchDataRealTime() {
        String matchDetailLiveUrl = hostServer + "/match/detail_live?user=" + apiUser + "&secret=" + apiSecret;
        ParameterizedTypeReference<APIResponse<List<StatisticalResponse>>> responseType = new ParameterizedTypeReference<APIResponse<List<StatisticalResponse>>>() {
        };
        ResponseEntity<APIResponse<List<StatisticalResponse>>> responseEntity = restTemplate.exchange(matchDetailLiveUrl, HttpMethod.GET, null, responseType);
        APIResponse<List<StatisticalResponse>> realTimeResponse = responseEntity.getBody();

        long currentTime = System.currentTimeMillis() / 1000;
        for (StatisticalResponse item : realTimeResponse.getResults()) {

            Match match = matches.get(item.getId());

            if (match != null) {
                int time = (int) (currentTime - match.getMatch_time()) / 60;
                if (match != null && match.getStatistics() == null) {
                    List<TimeWithStatistic> statistics = new ArrayList<>();
                    match.setStatistics(statistics);
                }
                if (time == 4 && !checkTypeExist(match, TimeType.FOUR_MINUTES)) {
                    TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                    timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                    timeWithStatistic.setStatistics(item.getStats());
                    timeWithStatistic.setType(TimeType.FOUR_MINUTES);
                    match.getStatistics().add(timeWithStatistic);
                    System.out.println("4 - "+match.getId());
                }
                if (time == 5 && !checkTypeExist(match, TimeType.FIVE_MINUTES)) {
                    TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                    timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                    timeWithStatistic.setStatistics(item.getStats());
                    timeWithStatistic.setType(TimeType.FIVE_MINUTES);
                    match.getStatistics().add(timeWithStatistic);
                    System.out.println("5 - "+match.getId());
                }
                if (time == 8 && !checkTypeExist(match, TimeType.EIGHT_MINUTES)) {
                    TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                    timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                    timeWithStatistic.setStatistics(item.getStats());
                    timeWithStatistic.setType(TimeType.EIGHT_MINUTES);
                    match.getStatistics().add(timeWithStatistic);
                    System.out.println("8 - "+match.getId());
                }
                if (time == 10 && !checkTypeExist(match, TimeType.TEN_MINUTES)) {
                    TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                    timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                    timeWithStatistic.setStatistics(item.getStats());
                    timeWithStatistic.setType(TimeType.TEN_MINUTES);
                    match.getStatistics().add(timeWithStatistic);
                    System.out.println("10 - "+match.getId());
                }

                match.setUpdated_at((int) System.currentTimeMillis() / 1000);
            }

        }
        return new ResponseEntity(realTimeResponse, HttpStatus.OK);
    }

    public boolean checkTypeExist(Match match, TimeType timeType) {

        for (TimeWithStatistic timeWithStatistic : match.getStatistics()) {
            if (timeWithStatistic.getType() == timeType) {
                return true;
            }
        }

        return false;
    }


    // fetch data all matches of date
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight (00:00) every day
    public void myScheduledMethod() {
        fetchDataMatch();
    }

    @Scheduled(fixedDelay = 2000) // Executes the method every 2 seconds after the previous execution completes
    public void getRealtimeData() {
        fetchDataRealTime();
    }
}
