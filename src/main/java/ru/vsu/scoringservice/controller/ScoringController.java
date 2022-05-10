package ru.vsu.scoringservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vsu.scoringservice.model.Person;
import ru.vsu.scoringservice.service.ScoreService;

@Controller
public class ScoringController {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/status")
    public ResponseEntity status() {
        System.out.println("Ok");
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "score")
    public String score(Person person) {
        var result = scoreService.score(person);
        return result.toString();
    }
}
