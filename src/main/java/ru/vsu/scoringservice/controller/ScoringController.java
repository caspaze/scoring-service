package ru.vsu.scoringservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vsu.scoringservice.model.Person;
import ru.vsu.scoringservice.service.ScoreService;

import java.util.Map;

@Controller
public class ScoringController {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/")
    public String index() {
        return "index";
    }



    @GetMapping(value = "score")
    public String score(Person person, Map<String,Object> model) {

        var result = scoreService.score(person);
        String rusResult = switch (result.getScoreResult()) {
            case "good" -> "Одобрено";
            default -> "Отказ";
        };
        model.put("result","Результат: " + rusResult);
        return "index";
    }
}
