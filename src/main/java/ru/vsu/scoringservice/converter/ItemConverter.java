package ru.vsu.scoringservice.converter;

import org.springframework.stereotype.Component;
import ru.vsu.scoringservice.decisiontree.Item;
import ru.vsu.scoringservice.model.Person;

@Component
public class ItemConverter {
    public Item convertPerson(Person person) {
        var item = new Item();
        item.setAttribute("checking_status", person.getCheckingStatus());
        item.setAttribute("duration", person.getDuration());
        item.setAttribute("credit_history", person.getCreditHistory());
        item.setAttribute("purpose", person.getPurpose());
        item.setAttribute("credit_amount", person.getCreditAmount());
        item.setAttribute("savings_status", person.getSavingStatus());
        item.setAttribute("employment", person.getEmployment());
        item.setAttribute("installment_commitment", person.getInstallmentCommitment());
        item.setAttribute("personal_status", person.getPersonalStatus());
        item.setAttribute("other_parties", person.getOtherParties());
        item.setAttribute("residence_since", person.getResidenceSince());
        item.setAttribute("property_magnitude", person.getPropertyMagnitude());
        item.setAttribute("age", person.getAge());
        item.setAttribute("other_payment_plans", person.getOtherPaymentPlans());
        item.setAttribute("housing", person.getHousing());
        item.setAttribute("existing_credits", person.getExistingCredits());
        item.setAttribute("job", person.getJob());
        item.setAttribute("num_dependents", person.getNumDependents());
        item.setAttribute("own_telephone", person.getOwnTelephone());
        item.setAttribute("foreign_worker", person.getForeignWorker());

        return item;
    }
}
