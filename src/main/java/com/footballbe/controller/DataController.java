package com.footballbe.controller;

import com.footballbe.dto.request.AccountRequest;
import com.footballbe.entity.Account;
import com.footballbe.enums.TimeType;
import com.footballbe.object.*;
import com.footballbe.repository.AccountRepository;
import com.footballbe.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
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

    @Autowired
    EmailService emailService;

    @Autowired
    AccountRepository accountRepository;

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

    @GetMapping("/matches2")
    public ResponseEntity getMatches2() {
        return new ResponseEntity(matches, HttpStatus.OK);
    }

    @GetMapping("/fetch")
    public void fetchDataMatch() {
        String matchDiaryUrl = hostServer + "/match/diary?user=" + apiUser + "&secret=" + apiSecret;
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
                if (match != null && match.getStatistics() == null) {
                    List<TimeWithStatistic> statistics = new ArrayList<>();
                    match.setStatistics(statistics);
                }
                if (match.getHandicap() == -1000) {
                    String handicapUrl = hostServer + "odds/history?user=" + apiUser + "&secret=" + apiSecret + "&uuid=" + match.getId();
                    ParameterizedTypeReference<APIResponse<Map<Integer, Map<String, List<List<Object>>>>>> responseHandicap = new ParameterizedTypeReference<APIResponse<Map<Integer, Map<String, List<List<Object>>>>>>() {
                    };
                    ResponseEntity<APIResponse<Map<Integer, Map<String, List<List<Object>>>>>> responseHandicapEntity = restTemplate.exchange(handicapUrl, HttpMethod.GET, null, responseHandicap);

                    try {
                        if (responseHandicapEntity.getBody().getResults() != null) {
                            match.setHandicap((double) responseHandicapEntity.getBody().getResults().get(2).get("asia").get(0).get(3));
                            match.setScoreHome((int) responseHandicapEntity.getBody().getResults().get(2).get("asia").get(0).get(3));
                            match.setHandicap((double) responseHandicapEntity.getBody().getResults().get(2).get("asia").get(0).get(3));
                        }
                    } catch (Exception e) {
                    }
                }

                if (match.getActualStartTime() == 0) {
                    match.setActualStartTime((int) item.getScore().get(4));
                }

                if (match.getActualStartTime() != 0) {
                    Instant currentTimestamp = Instant.now();
                    long unixTimestamp = currentTimestamp.getEpochSecond();
                    int time = (int) (currentTime - match.getActualStartTime()) / 60;
                    System.out.println(match.getId()+" - "+time);
                    if (time == 4 && !checkTypeExist(match, TimeType.FOUR_MINUTES)) {
                        TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                        timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                        timeWithStatistic.setStatistics(item.getStats());
                        timeWithStatistic.setType(TimeType.FOUR_MINUTES);
                        match.getStatistics().add(timeWithStatistic);
                        System.out.println("4 - " + match.getId());
                    }
                    if (time == 5 && !checkTypeExist(match, TimeType.FIVE_MINUTES)) {
                        TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                        timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                        timeWithStatistic.setStatistics(item.getStats());
                        timeWithStatistic.setType(TimeType.FIVE_MINUTES);
                        match.getStatistics().add(timeWithStatistic);
                        System.out.println("5 - " + match.getId());
                    }
                    if (time == 8 && !checkTypeExist(match, TimeType.EIGHT_MINUTES)) {
                        TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                        timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                        timeWithStatistic.setStatistics(item.getStats());
                        timeWithStatistic.setType(TimeType.EIGHT_MINUTES);
                        match.getStatistics().add(timeWithStatistic);
                        System.out.println("8 - " + match.getId());
                    }
                    if (time == 10 && !checkTypeExist(match, TimeType.TEN_MINUTES)) {
                        TimeWithStatistic timeWithStatistic = new TimeWithStatistic();
                        timeWithStatistic.setTime(System.currentTimeMillis() / 1000);
                        timeWithStatistic.setStatistics(item.getStats());
                        timeWithStatistic.setType(TimeType.TEN_MINUTES);
                        match.getStatistics().add(timeWithStatistic);
                        System.out.println("10 - " + match.getId());
                    }
                }


                try {
                    match.setMatchState((int) item.getScore().get(1));
                    match.setScoreHome(((List<Integer>) item.getScore().get(2)).get(1));
                    match.setScoreAway(((List<Integer>) item.getScore().get(3)).get(1));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
//                match.setUpdated_at((int) System.currentTimeMillis() / 1000);
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


    @PostMapping("mail/{matchId}/{type}")
    public void sendMail(@PathVariable String matchId, @PathVariable String type) {
        Match match = matches.get(matchId);

        if (type.equals("5_Minutes") && match.isNotification1()) return;
        if (type.equals("5-4_Minutes") && match.isNotification2()) return;
        if (type.equals("10_Minutes") && match.isNotification3()) return;
        if (type.equals("10-8_Minutes") && match.isNotification4()) return;

        List<Account> accounts = accountRepository.findActiveAccount();
        for (Account account : accounts) {
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setSubject(type);
            emailDetail.setMsgBody(match.getId() + " - " + type + " - " + match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName());
            emailDetail.setRecipient(account.getEmail());
            if(!account.isCheck1() && type.equals("5_Minutes")){
                continue;
            }
            if(!account.isCheck2() && type.equals("5-4_Minutes")){
                continue;
            }
            if(!account.isCheck3() && type.equals("10_Minutes")){
                continue;
            }
            if(!account.isCheck4() && type.equals("10-8_Minutes")){
                continue;
            }
            System.out.println("send mail to " + account.getEmail());
            emailService.sendSimpleMail(emailDetail);
        }
        if (type.equals("5_Minutes")) match.setNotification1(true);
        if (type.equals("5-4_Minutes")) match.setNotification2(true);
        if (type.equals("10_Minutes")) match.setNotification3(true);
        if (type.equals("10-8_Minutes")) match.setNotification4(true);
    }

    @GetMapping("account")
    public ResponseEntity getAccount(){
        return new ResponseEntity(accountRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("account")
    public ResponseEntity createAccount(@RequestBody Account account){
        return new ResponseEntity(accountRepository.save(account), HttpStatus.OK);
    }

    @PutMapping("account")
    public ResponseEntity updateAccount(@RequestBody AccountRequest accounts){
        return new ResponseEntity(accountRepository.saveAll(accounts.getAccounts()), HttpStatus.OK);
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
