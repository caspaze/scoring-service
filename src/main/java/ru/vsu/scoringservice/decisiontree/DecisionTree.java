package ru.vsu.scoringservice.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DecisionTree {

	private Rule rule;

	private Object category;

	private DecisionTree matchSubTree;

	private DecisionTree notMatchSubTree;

	public Object classify(Item item) {
		if (this.isLeaf()) {
			return this.getCategory();
		} else {
			if (this.getRule().match(item)) {
				return this.getMatchSubTree().classify(item);
			} else {
				return this.getNotMatchSubTree().classify(item);
			}
		}
	}

	public DecisionTree mergeRedundantRules() {

		if (this.getMatchSubTree() != null) {
			this.getMatchSubTree().mergeRedundantRules();
		}

		if (this.getNotMatchSubTree() != null) {
			this.getNotMatchSubTree().mergeRedundantRules();
		}

		if ((this.getRule() != null)
				&& (this.getMatchSubTree().getCategory() != null)
				&& (this.getNotMatchSubTree().getCategory() != null)
				&& (this.getMatchSubTree().getCategory().equals(this.getNotMatchSubTree().getCategory()))) {

			this.setCategory(this.getMatchSubTree().getCategory());
			this.setRule(null);
			this.setMatchSubTree(null);
			this.setNotMatchSubTree(null);
		}

		return this;
	}

    public void accept(DecisionTreeVisitor visitor) {
		visitor.visit(this);
	}

	public boolean isLeaf() {
		return this.getRule() == null;
	}

	public Object getCategory() {
		return this.category;
	}

	public Rule getRule() {
		return this.rule;
	}

	public DecisionTree getMatchSubTree() {
		return this.matchSubTree;
	}

	public DecisionTree getNotMatchSubTree() {
		return this.notMatchSubTree;
	}

	public static DecisionTreeBuilder createBuilder() {
		return new DecisionTreeBuilder();
	}

	public static DecisionTree buildDecisionTree(
			List<Item> items,
			int minimalNumberOfItems,
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates,
			Set<String> ignoredAttributes
    ) {

		if (items.size() <= minimalNumberOfItems) {
			return makeLeaf(items);
		}

		double entropy = entropy(items);

		if (Double.compare(entropy, 0) == 0) {
			return makeLeaf(items);
		}

		SplitResult splitResult = findBestSplit(items, attributesPredicates, defaultPredicates, ignoredAttributes);

		if (splitResult == null) {
			return makeLeaf(items);
		}

		DecisionTree matchSubTree =
				buildDecisionTree(splitResult.matched, minimalNumberOfItems, attributesPredicates, defaultPredicates, ignoredAttributes);

		DecisionTree notMatchSubTree =
				buildDecisionTree(splitResult.notMatched, minimalNumberOfItems, attributesPredicates, defaultPredicates, ignoredAttributes);

		DecisionTree root = new DecisionTree();
		root.setRule(splitResult.rule);
		root.setMatchSubTree(matchSubTree);
		root.setNotMatchSubTree(notMatchSubTree);
		return root;
	}

	private static DecisionTree makeLeaf(List<Item> items) {
		List<Object> categories = getCategories(items);
		Object category = getMostFrequentCategory(categories);
		DecisionTree leaf = new DecisionTree();
		leaf.setCategory(category);
		return leaf;
	}

	private static Object getMostFrequentCategory(List<Object> categories) {
		Map<Object, Integer> categoryCountMap = groupAndCount(categories);

		Object mostFrequentCategory = null;
		int mostFrequentCategoryCount = -1;

		for (Entry<Object, Integer> e : categoryCountMap.entrySet()) {
			Object category = e.getKey();
			int count = e.getValue();

			if (count >= mostFrequentCategoryCount) {
				mostFrequentCategoryCount = count;
				mostFrequentCategory = category;
			}
		}

		return mostFrequentCategory;
	}

	private static SplitResult findBestSplit(
			List<Item> items,
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates,
			Set<String> ignoredAttributes) {
		
		double initialEntropy = entropy(items);

		double bestGain = 0;

		SplitResult bestSplitResult = null;
		
		Set<Rule> testedRules = new HashSet<>();
		
		for (Item baseItem : new LinkedList<>(items)) {
			for (String attr : baseItem.getAttributeNames()) {

				if (ignoredAttributes.contains(attr)) {
					continue;
				}

				Object value = baseItem.getFieldValue(attr);

				List<Predicate> predicates = predicatesForAttribute(attr, attributesPredicates, defaultPredicates);

				for (Predicate pred : predicates) {
					Rule rule = new Rule(attr, pred, value);

					if(testedRules.contains(rule)) {
						continue;
					}
					testedRules.add(rule);

					SplitResult splitResult = split(rule, items);

					double matchedEntropy = entropy(splitResult.matched);
					double notMatchedEntropy = entropy(splitResult.notMatched);

					double pMatched = (double) splitResult.matched.size() / items.size();
					double pNotMatched = (double) splitResult.notMatched.size() / items.size();

					double gain = initialEntropy - (pMatched * matchedEntropy) - (pNotMatched * notMatchedEntropy);
					if (gain > bestGain) {
						bestGain = gain;
						bestSplitResult = splitResult;
					}
				}
			}
		}
		return bestSplitResult;
	}

	private static SplitResult split(Rule rule, List<Item> base) {
		List<Item> matched = new LinkedList<>();
		List<Item> notMatched = new LinkedList<>();

		for (Item otherItem : new LinkedList<>(base)) {
			if (rule.match(otherItem)) {
				matched.add(otherItem);
			} else {
				notMatched.add(otherItem);
			}
		}

		return new SplitResult(matched, notMatched, rule);
	}

	private static List<Predicate> predicatesForAttribute(
			String attr,
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates) {

		List<Predicate> attrPredicates = attributesPredicates.get(attr);
		if ((attrPredicates != null) && (!attrPredicates.isEmpty())) {
			return attrPredicates;
		} else if ((defaultPredicates != null) && (!defaultPredicates.isEmpty())) {
			return defaultPredicates;
		}

		return Collections.emptyList();
	}

	private static double entropy(List<Item> items) {
		List<Object> categories = getCategories(items);
		Map<Object, Integer> categoryCount = groupAndCount(categories);
		return entropy(categoryCount.values());
	}

	private static double entropy(Collection<Integer> values) {
		double totalCount = 0;
		for (Integer count : values) {
			totalCount += count;
		}

		double entropy = 0;
		for (Integer count : values) {
			double p = count / totalCount;
			entropy += -1 * p * Math.log(p);
		}
		return entropy;
	}

	private static List<Object> getCategories(List<Item> items) {
		List<Object> categories = new LinkedList<>();
		for (Item item : items) {
			categories.add(item.getCategory());
		}
		return categories;
	}

	private static Map<Object, Integer> groupAndCount(List<Object> categories) {
		Map<Object, Integer> categoryCount = new HashMap<>();
		for (Object cat : categories) {
			Integer count = categoryCount.get(cat);
			if (count == null) {
				count = 0;
			}
			categoryCount.put(cat, count + 1);
		}
		return categoryCount;
	}

	public void setCategory(Object category) {
		this.category = category;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setMatchSubTree(DecisionTree matchSubTree) {
		this.matchSubTree = matchSubTree;
	}

	public void setNotMatchSubTree(DecisionTree notMatchSubTree) {
		this.notMatchSubTree = notMatchSubTree;
	}

	private record SplitResult(List<Item> matched,
							   List<Item> notMatched,
							   Rule rule) {

		private SplitResult(List<Item> matched, List<Item> notMatched, Rule rule) {
			this.rule = rule;
			this.matched = new ArrayList<>(matched);
			this.notMatched = new ArrayList<>(notMatched);
		}
	}

}
