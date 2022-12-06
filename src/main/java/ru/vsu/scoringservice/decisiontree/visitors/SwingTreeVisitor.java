package ru.vsu.scoringservice.decisiontree.visitors;

import ru.vsu.scoringservice.decisiontree.DecisionTree;
import ru.vsu.scoringservice.decisiontree.DecisionTreeVisitor;
import ru.vsu.scoringservice.decisiontree.Predicate;

import javax.swing.tree.DefaultMutableTreeNode;

public class SwingTreeVisitor implements DecisionTreeVisitor {

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    @Override
    public void visit(DecisionTree tree) {
        if (tree.getRule() != null) {
            String description;

            Predicate predicate = tree.getRule().predicate();
            String attribute = tree.getRule().attribute();
            Object value = tree.getRule().sampleValue();
            description = switch (predicate) {
                case EQUAL -> attribute + " == " + value;
                case EXISTS -> "exists " + attribute;
                case GTE -> attribute + " >= " + value;
                case LTE -> attribute + " =< " + value;
            };

            this.root.setUserObject(description);
            this.root.add( buildSwingTree( tree.getMatchSubTree() ) );
            this.root.add( buildSwingTree( tree.getNotMatchSubTree() ) );
        } else {
            this.root.setUserObject(tree.getCategory().toString());
        }
    }

    public DefaultMutableTreeNode getRoot() {
        return this.root;
    }

    public static DefaultMutableTreeNode buildSwingTree( DecisionTree tree ) {
        SwingTreeVisitor visitor = new SwingTreeVisitor();
        tree.accept( visitor );
        return visitor.getRoot();
    }
}
