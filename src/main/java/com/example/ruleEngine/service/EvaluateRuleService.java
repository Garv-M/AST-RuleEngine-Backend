package com.example.ruleEngine.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaluateRuleService {

    // Recursive function to evaluate each node in the AST
    public boolean evaluateNode(Map<String, Object> node, Map<String, Object> data) {
        String type = (String) node.get("type");

        // If the node is an operand, evaluate the condition
        if ("OPERAND".equals(type)) {
            String condition = (String) node.get("value");
            return evaluateCondition(condition, data);
        }

        // If the node is an operator, recursively evaluate left and right child nodes
        if ("OPERATOR".equals(type)) {
            String operator = (String) node.get("operator");
            Map<String, Object> leftNode = (Map<String, Object>) node.get("left");
            Map<String, Object> rightNode = (Map<String, Object>) node.get("right");

            boolean leftResult = evaluateNode(leftNode, data);
            boolean rightResult = evaluateNode(rightNode, data);

            if ("AND".equals(operator)) {
                return leftResult && rightResult;
            } else if ("OR".equals(operator)) {
                return leftResult || rightResult;
            }
        }

        return false;
    }

    // Function to evaluate a single condition (e.g., "age > 30") against the data
    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        // Regex to capture the left operand, operator, and right operand
        String regex = "([a-zA-Z_]+)\\s*(>|<|=)\\s*('?\\w+'?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(condition);

        if (matcher.find()) {
            String attribute = matcher.group(1);  // The left operand (e.g., "age")
            String operator = matcher.group(2);   // The operator (e.g., ">", "<", "=")
            String value = matcher.group(3).replace("'", ""); // The right operand (e.g., "30" or "Sales")

            // Get the attribute's actual value from the data
            Object actualValue = data.get(attribute);

            // Compare based on the operator
            if (actualValue instanceof Integer) {
                int actual = (int) actualValue;
                int expected = Integer.parseInt(value);
                switch (operator) {
                    case ">":
                        return actual > expected;
                    case "<":
                        return actual < expected;
                    case "=":
                        return actual == expected;
                }
            } else if (actualValue instanceof String) {
                String actual = (String) actualValue;
                switch (operator) {
                    case "=":
                        return actual.equals(value);
                }
            }
        }

        return false;  // If the condition or operator is invalid
    }
}
