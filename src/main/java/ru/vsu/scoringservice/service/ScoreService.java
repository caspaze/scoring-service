package ru.vsu.scoringservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.scoringservice.converter.ItemConverter;
import ru.vsu.scoringservice.converter.PersonConverter;
import ru.vsu.scoringservice.decisiontree.RandomForest;
import ru.vsu.scoringservice.model.Person;
import ru.vsu.scoringservice.model.Result;

import java.util.Collections;
import java.util.Map;

@Service
public class ScoreService {
    @Autowired
    private TrainService trainService;
    @Autowired
    private RandomForest randomForest;
    @Autowired
    private ItemConverter itemConverter;
    @Autowired
    private PersonConverter personConverter;

    public Result score(Person person) {
        person = personConverter.adaptPersonModel(person);
        var item = itemConverter.convertPerson(person);
        var map = randomForest.classify(item);
        return new Result((String) decide(map));
    }

    private Object decide(Map<Object, Integer> results) {
        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
