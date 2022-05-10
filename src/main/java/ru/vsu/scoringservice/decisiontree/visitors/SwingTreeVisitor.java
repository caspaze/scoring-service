package ru.vsu.scoringservice.decisiontree.visitors;

import ru.vsu.scoringservice.decisiontree.DecisionTree;
import ru.vsu.scoringservice.decisiontree.DecisionTreeVisitor;
import ru.vsu.scoringservice.decisiontree.Predicate;

import javax.swing.tree.DefaultMutableTreeNode;

public class SwingTreeVisitor implements DecisionTreeVisitor {

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    @Override
    public void visit(DecisionTree tree) {
        if (tree.getRule() != null) {
            String description;

            Predicate predicate = tree.getRule().getPredicate();
            String attribute = tree.getRule().getAttribute();
            Object value = tree.getRule().getSampleValue();
            switch (predicate) {
                case EQUAL:
                    description = attribute + " == " + value;
                    break;

                case EXISTS:
                    description = "exists " + attribute;
                    break;

                case GTE:
                    description = attribute + " >= " + value;
                    break;

                case LTE:
                    description = attribute + " =< " + value;
                    break;

                default:
                    description = attribute + predicate.toString() + value;
                    break;
            }

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
