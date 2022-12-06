package ru.vsu.scoringservice.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.vsu.scoringservice.decisiontree.DecisionTree;
import ru.vsu.scoringservice.decisiontree.DecisionTreeBuilder;
import ru.vsu.scoringservice.decisiontree.Item;
import ru.vsu.scoringservice.decisiontree.Predicate;
import ru.vsu.scoringservice.decisiontree.RandomForest;
import ru.vsu.scoringservice.decisiontree.visitors.SwingTreeVisitor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {
    @Value("${decisiontree.trainset}")
    private String path;
    @Value("${decisiontree.displayforest}")
    private Boolean displayForest;
    @Value("${decisiontree.treesAmount}")
    private Integer treesAmount;

    @Bean
    public RandomForest train() {
        var trainSet = uploadData();
        DecisionTreeBuilder tbuilder =
                DecisionTree
                        .createBuilder()
                        .setDefaultPredicates(Predicate.EQUAL)
                        .setAttributePredicates("duration", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("credit_amount", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("installment_commitment", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("residence_since", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("age", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("existing_credits", Predicate.GTE, Predicate.LTE)
                        .setAttributePredicates("num_dependents", Predicate.GTE, Predicate.LTE)
                        .setTrainingSet(trainSet);
        RandomForest forest = tbuilder.createRandomForest(treesAmount);
        if (displayForest) {
            displayForest(forest);
        }
        return forest;
    }

    @SneakyThrows
    private List<Item> uploadData() {
        var reader = new CSVReader(new FileReader(path), ',' , '"' , 1);
        List<Item> list = new ArrayList<>();
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            var item = new Item();
            item.setAttribute("checking_status", nextLine[0]);
            item.setAttribute("duration", Integer.valueOf(nextLine[1]));
            item.setAttribute("credit_history", nextLine[2]);
            item.setAttribute("purpose", nextLine[3]);
            item.setAttribute("credit_amount", Integer.valueOf(nextLine[4]));
            item.setAttribute("savings_status", nextLine[5]);
            item.setAttribute("employment", nextLine[6]);
            item.setAttribute("installment_commitment", Integer.valueOf(nextLine[7]));
            item.setAttribute("personal_status", nextLine[8]);
            item.setAttribute("other_parties", nextLine[9]);
            item.setAttribute("residence_since", Integer.valueOf(nextLine[10]));
            item.setAttribute("property_magnitude", nextLine[11]);
            item.setAttribute("age", Integer.valueOf(nextLine[12]));
            item.setAttribute("other_payment_plans", nextLine[13]);
            item.setAttribute("housing", nextLine[14]);
            item.setAttribute("existing_credits", Integer.valueOf(nextLine[15]));
            item.setAttribute("job", nextLine[16]);
            item.setAttribute("num_dependents", Integer.valueOf(nextLine[17]));
            item.setAttribute("own_telephone", nextLine[18]);
            item.setAttribute("foreign_worker", nextLine[19]);
            item.setCategory(nextLine[20]);
            list.add(item);
        }
        return list;
    }

    public void displayForest(RandomForest forest) {
        forest.getTrees().forEach(tree -> display(SwingTreeVisitor.buildSwingTree(tree), 400, 500));
        //display(SwingTreeVisitor.buildSwingTree(forest.getTrees().get(1)), 400, 500);
    }

    private JFrame display(DefaultMutableTreeNode root, int width, int height) {
        JTree tree = new JTree(root);

        JFrame frame = new JFrame();
        frame.add(new JScrollPane(tree), BorderLayout.CENTER);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        frame.setSize(width, height);
        // put frame at center of screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
}
