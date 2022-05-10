package ru.vsu.scoringservice.converter;

import org.springframework.stereotype.Component;
import ru.vsu.scoringservice.model.Person;

import java.util.Map;

@Component
public class PersonConverter {

    public Person adaptPersonModel(Person person) {
        person.setCheckingStatus(adaptCheckingStatus(person.getCheckingStatus()));
        person.setCreditHistory(adaptCreditHistory(person.getCreditHistory()));
        person.setPurpose(adaptPurpose(person.getPurpose()));
        person.setSavingStatus(adaptSavingStatus(person.getSavingStatus()));
        person.setEmployment(employmentCorMap.get(person.getEmployment()));
        person.setPersonalStatus(personalStatusCorMap.get(person.getPersonalStatus()));
        person.setOtherParties(otherPartiesCorMap.get(person.getOtherParties()));
        person.setPropertyMagnitude(propertyMagnitudeCorMap.get(person.getPropertyMagnitude()));
        person.setOtherPaymentPlans(otherPaymentPlansCorMap.get(person.getOtherPaymentPlans()));
        person.setHousing(housingCorMap.get(person.getHousing()));
        person.setJob(jobCorMap.get(person.getJob()));
        person.setOwnTelephone(ownTelephoneCorMap.get(person.getOwnTelephone()));
        person.setForeignWorker(foreignWorkerCorMap.get(person.getForeignWorker()));

        return person;
    }

    private final Map<String, String> employmentCorMap = Map.of(
            "Не работает", "unemployed",
            "< 1 года", "<1",
            "1 <= ... < 4 лет", "1<=X<4",
            "4 <= ... < 7 лет", "4<=X<7",
            ">= 7 лет", ">=7");

    private final Map<String, String> personalStatusCorMap = Map.of(
            "Мужчина: женат", "male mar/wid",
            "Мужчина: разведен/живет отдельно", "male div/sep",
            "Мужчина: не в браке", "male single",
            "Женщина: замужем", "female div/dep/mar",
            "Женщина: разведена/живет отдельно", "female div/dep/mar",
            "Женщина: не в браке", "female div/dep/mar");

    private final Map<String, String> otherPartiesCorMap = Map.of(
            "Отсутствует", "none",
            "Созаявитель", "co applicant",
            "Поручитель", "guarantor");

    private final Map<String, String> propertyMagnitudeCorMap = Map.of(
            "Недвижимость", "real estate",
            "Страхование жизни", "life insurance",
            "Автомобиль", "car",
            "Неизвестно / Нету собственности", "no known property");

    private final Map<String, String> otherPaymentPlansCorMap = Map.of(
            "Банк", "bank",
            "Сбережения", "stores",
            "Отсвутствует", "none");

    private final Map<String, String> housingCorMap = Map.of(
            "Аренда", "rent",
            "Собственное", "own",
            "Бесплатное", "for free");

    private final Map<String, String> jobCorMap = Map.of(
            "Безработный/неквалифицированный - нерезидент", "unemp/unskilled non res",
            "Неквалифицированный - резидент", "unskilled resident",
            "Квалифицированный работник / Должностное лицо", "skilled",
            "Руководитель/Самозанятый/Высококвалифицированный сотрудник", "high qualif/self emp/mgmt");

    private final Map<String, String> ownTelephoneCorMap = Map.of(
            "Отсутствует", "none",
            "Имеется, зарегистрирован на имя клиента", "yes");

    private final Map<String, String> foreignWorkerCorMap = Map.of(
            "Да", "yes",
            "Нет", "no");

    private String adaptCheckingStatus(String checkingStatus) {
        if ("Нет расчетного счета".equals(checkingStatus)) {
            return "no checking";
        }
        return checkingStatus;
    }

    private String adaptCreditHistory(String creditHistory) {
        return switch (creditHistory) {
            case "Кредиты не взяты / Все кредиты возвращены должным образом" -> "no credits/all paid";
            case "Все кредиты в этом банке погашены должным образом" -> "all paid";
            case "Существующие кредиты, погашющиеся должным образом" -> "existing paid";
            case "Прошлые кредиты с задержками в выплатах" -> "delayed previously";
            case "Критический счет / Другие существующие кредиты (не в этом банке)" -> "critical/other existing credit";
            default -> creditHistory;
        };
    }

    private String adaptPurpose(String purpose) {
        return switch (purpose) {
            case "Автомобиль (новый)" -> "new car";
            case "Автомобиль (б/у)" -> "used car";
            case "Мебель / Оборудование" -> "furniture/equipment";
            case "Компьютер / Телевизор" -> "radio/tv";
            case "Бытовая техника" -> "domestic appliance";
            case "Ремонт" -> "repairs";
            case "Образование" -> "education";
            case "Путешествие" -> "vacation";
            case "Бизнес" -> "business";
            case "Другое" -> "other";
            default -> purpose;
        };
    }

    private String adaptSavingStatus(String savingStatus) {
        return switch (savingStatus) {
            case "< 100" -> "<100";
            case "100 <= ... < 500" -> "100<=X<500";
            case "500 <= ... < 1000" -> "100<=X<500";
            case "... >= 1000" -> ">=1000";
            case "Нет сбережений" -> "no known savings";
            default -> savingStatus;
        };
    }


}
