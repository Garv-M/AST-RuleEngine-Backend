package com.example.ruleEngine.service;

import com.example.ruleEngine.exception.InvalidRuleFormatException;
import com.example.ruleEngine.model.Node;
import com.example.ruleEngine.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private NodeRepository nodeRepository;
    private static final Set<String> ATTRIBUTE_CATALOG = Set.of("age", "department", "salary", "experience");
    RuleParser parser = new RuleParser();

    public Node createRule(String ruleString) {
        validateRuleSyntax(ruleString);
        RuleParser parser = new RuleParser();
        Node rootNode = parser.parseRule(ruleString); // Parsing logic
        return nodeRepository.save(rootNode);  // Persist in MongoDB
    }

    public Node combine(List<Node> ruleASTs){
        CombineRulesService combineRulesService = new CombineRulesService();
        String topOperator = combineRulesService.determineTopOperator(ruleASTs);

        Node combinedAST = combineRulesService.combineASTs(ruleASTs, topOperator);
        return nodeRepository.save(combinedAST);
    }

    public Node combineRules(List<String> rules) {
        List<Node> ruleASTs = new ArrayList<>();
        for (String rule : rules) {
            validateRuleSyntax(rule);
            Node ast = parser.parseRule(rule);
            ruleASTs.add(ast);
        }

        return combine(ruleASTs);
    }

    public List<Map<String, String>> getAllRulesAsString() {
        List<Node> rules = nodeRepository.findAll();
        return rules.stream()
                .map(rule -> Map.of(
                        "id", String.valueOf(rule.getId()),
                        "rule", astToString(rule)
                ))
                .collect(Collectors.toList());
    }


    public boolean evaluateRule(Map<String, Object> ruleAST, Map<String, Object> data) {
        EvaluateRuleService evaluateRuleService = new EvaluateRuleService();
        return evaluateRuleService.evaluateNode(ruleAST, data);
    }

    public String astToString(Node node) {
        if (node == null) return "";

        if (node.getType() == Node.NodeType.OPERAND) {
            return node.getValue();  // Return the value of the operand directly
        } else if (node.getType() == Node.NodeType.OPERATOR) {
            String left = astToString(node.getLeft());
            String right = astToString(node.getRight());

            return "(" + left + " " + node.getOperator() + " " + right + ")";
        }
        return "";
    }


    public Node UpdateRule(String ruleString, String ruleId) {
        validateRuleSyntax(ruleString);
        Node newAst = parser.parseRule(ruleString);
        if (ruleId != null) {
            updateRuleInDatabase(ruleId, newAst);
        }

        return newAst;
    }

    public Node getRuleById(String ruleId) {
        return nodeRepository.findById(ruleId)
                .orElseThrow(() -> new InvalidRuleFormatException("Rule not found with ID: " + ruleId));
    }

    private void updateRuleInDatabase(String ruleId, Node updatedAst) {
        Node existingNode = getRuleById(ruleId);

        existingNode.setLeft(updatedAst.getLeft());
        existingNode.setRight(updatedAst.getRight());
        existingNode.setOperator(updatedAst.getOperator());
        existingNode.setValue(updatedAst.getValue());
        existingNode.setType(updatedAst.getType());

        nodeRepository.save(existingNode);
    }

    private void validateRuleSyntax(String ruleString) {
        if (ruleString == null || ruleString.trim().isEmpty()) {
            throw new InvalidRuleFormatException("Rule cannot be empty.");
        }

        if (!validateParentheses(ruleString)) {
            throw new InvalidRuleFormatException("Mismatched parentheses in the rule.");
        }

        String sanitizedRule = ruleString.replaceAll("[()]", "");
        String[] comparisons = sanitizedRule.split("\\s+(AND|OR)\\s+");

        for (String comparison : comparisons) {
            if (!isValidComparison(comparison.trim())) {
                throw new InvalidRuleFormatException("Rule contains invalid comparison syntax: " + comparison);
            }
        }

        validateAttributesInCatalog(ruleString);
    }

    private boolean validateParentheses(String ruleString) {
        int openCount = 0;
        for (char ch : ruleString.toCharArray()) {
            if (ch == '(') openCount++;
            else if (ch == ')') openCount--;
            if (openCount < 0) return false;
        }
        return openCount == 0;
    }

    // Validate individual comparison
    private boolean isValidComparison(String comparison) {
        String comparisonRegex = "\\w+\\s*(>|<|=)\\s*'?\\w+'?";
        return comparison.matches(comparisonRegex);
    }

    private void validateAttributesInCatalog(String ruleString) {
        Pattern pattern = Pattern.compile("\\b(\\w+)\\s*(>|<|=)");
        Matcher matcher = pattern.matcher(ruleString);

        while (matcher.find()) {
            String attribute = matcher.group(1);
            if (!ATTRIBUTE_CATALOG.contains(attribute)) {
                throw new InvalidRuleFormatException("Attribute '" + attribute + "' is not part of the catalog.");
            }
        }
    }


}
