package com.example.ruleEngine.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaluateRuleService {

    public boolean evaluateNode(Map<String, Object> node, Map<String, Object> data) {
        String type = (String) node.get("type");

        if ("OPERAND".equals(type)) {
            String condition = (String) node.get("value");
            return evaluateCondition(condition, data);
        }

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

    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        String regex = "([a-zA-Z_]+)\\s*(>|<|=)\\s*('?\\w+'?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(condition);

        if (matcher.find()) {
            String attribute = matcher.group(1);
            String operator = matcher.group(2);
            String value = matcher.group(3).replace("'", "");

            Object actualValue = data.get(attribute);

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

        return false;
    }
}
