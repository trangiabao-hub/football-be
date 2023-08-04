package com.footballbe.controller;

import com.footballbe.object.EmailDetail;
import com.footballbe.object.Match;
import com.footballbe.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class MailController {

    @Autowired
    EmailService emailService;

    @PostMapping("mail/{type}")
    public void sendMail(@RequestBody Match match, @PathVariable String type){
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setSubject(type);
        emailDetail.setMsgBody(match.getId()+ " - " + match.getHomeTeam().getName() + " vs "+match.getAwayTeam().getName());
        emailDetail.setRecipient("giabaotran912@gmail.com");
        emailService.sendSimpleMail(emailDetail);
    }

}
