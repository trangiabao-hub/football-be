package com.footballbe.controller;

import com.footballbe.dto.request.StatisticRequest;
import com.footballbe.dto.request.UnderrafterRequest;
import com.footballbe.dto.request.XRequest;
import com.footballbe.entity.X;
import com.footballbe.repository.StatisticRepository;
import com.footballbe.repository.UnderrafterRespository;
import com.footballbe.repository.XRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class StatisticController {

    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    XRepository xRepository;

    @Autowired
    UnderrafterRespository underrafterRespository;

    @GetMapping("statistic")
    public ResponseEntity getStatistic(){
        return new ResponseEntity(statisticRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("statistic")
    public ResponseEntity updateStatistic(@RequestBody StatisticRequest statisticRequest){
        statisticRepository.saveAll(statisticRequest.getStatistics());
        return new ResponseEntity(statisticRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("underrafter")
    public ResponseEntity getUnderrafter(){
        return new ResponseEntity(underrafterRespository.findAll(), HttpStatus.OK);
    }

    @PostMapping("underrafter")
    public ResponseEntity updateUnderrafter(@RequestBody UnderrafterRequest underrafterRequest){
        underrafterRespository.saveAll(underrafterRequest.getUnderrafterValues());
        return new ResponseEntity(statisticRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("x")
    public ResponseEntity getValue(){
        return new ResponseEntity(xRepository.findX(), HttpStatus.OK);
    }

    @PostMapping("x")
    public ResponseEntity updateX(@RequestBody XRequest xRequest){
        X x = xRepository.findX();
        x.setValue(xRequest.getValue());
        return new ResponseEntity(xRepository.save(x), HttpStatus.OK);
    }
}
