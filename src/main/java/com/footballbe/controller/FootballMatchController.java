package com.footballbe.controller;

import com.footballbe.dto.MatchData;
import com.footballbe.dto.RealTime;
import com.footballbe.dto.Statistical;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin("*")
public class FootballMatchController {

    @Value("${api.user}")
    private String apiUser;

    @Value("${api.secret}")
    private String apiSecret;

    @Value("${api.host-server}")
    private String hostServer;

    @GetMapping("")
    public ResponseEntity<List<MatchData.Result>> getAllMatches() {
        String date = "20230727";
        String matchDiaryUrl = hostServer + "/match/diary?user=" + apiUser + "&secret=" + apiSecret;
        String matchDetailLiveUrl = hostServer + "/match/detail_live?user=" + apiUser + "&secret=" + apiSecret + "&date=" + date;

        List<MatchData.Result> listMatchResponse = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MatchData> matchResponse = restTemplate.getForEntity(matchDiaryUrl, MatchData.class);
            if (matchResponse.getBody() != null) {
                matchResponse.getBody().mapTeams();
                matchResponse.getBody().mapCompetition();
                matchResponse.getBody().getQuery().setDate(date);

                Map<String, MatchData.Result> resultMap = new HashMap<>();
                matchResponse.getBody().getResults().forEach(item -> {
                    resultMap.put(item.getId(), item);
                });

                ResponseEntity<RealTime> realTimeResponse = restTemplate.getForEntity(matchDetailLiveUrl, RealTime.class);
                if (realTimeResponse.getBody() != null) {
                    realTimeResponse.getBody().getResults().forEach(item -> {
                        MatchData.Result result = resultMap.get(item.getId());
                        if (result != null) {
//                            result.setStatisticalResult(item);
                            listMatchResponse.add(result);
                        }
                    });
                }

                // Sort the list of matches by match time
                listMatchResponse.sort(Comparator.comparingLong(MatchData.Result::getMatch_time));

                return new ResponseEntity<>(listMatchResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            // Handle exceptions properly
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @Scheduled(fixedDelay = 2000) // Executes the method every 2 seconds after the previous execution completes
//    public void myScheduledMethod() {
//        System.out.println("This message is delayed by 2 seconds.");
//        // Add your logic here
//    }
}
