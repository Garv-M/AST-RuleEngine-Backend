package com.example.ruleEngine.service;

import com.example.ruleEngine.model.Node;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser {

    private List<String> tokens;
    private int currentTokenIndex;

    public Node parseRule(String ruleString) {
        this.tokens = tokenize(ruleString);
        this.currentTokenIndex = 0;

        return parseExpression();
    }

    private List<String> tokenize(String ruleString) {
        String regex = "\\s*(=>|AND|OR|\\(|\\)|>|<|=|'[a-zA-Z ]*'|[a-zA-Z0-9_]+)\\s*";
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(ruleString);
        while (matcher.find()) {
            tokens.add(matcher.group().trim());
        }

        return tokens;
    }

    private boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size();
    }
    private String peekToken() {
        return tokens.get(currentTokenIndex);
    }
    private String nextToken() {
        return tokens.get(currentTokenIndex++);
    }

    private Node parseExpression() {
        Node left = parseTerm();

        while (hasMoreTokens() && (peekToken().equals("AND") || peekToken().equals("OR"))) {
            String operator = nextToken();
            Node right = parseTerm();
            left = new Node(operator, left, right);
        }

        return left;
    }

    private Node parseTerm() {
        String token = nextToken();

        if (token.equals("(")) {
            Node subExpression = parseExpression();
            nextToken();
            return subExpression;
        }

        String leftOperand = token;
        if (hasMoreTokens() && (peekToken().equals(">") || peekToken().equals("<") || peekToken().equals("="))) {
            String operator = nextToken();
            String rightOperand = nextToken();
            return new Node(leftOperand + " " + operator + " " + rightOperand);
        }
        return new Node(leftOperand);
    }
}