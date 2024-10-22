package com.example.ruleEngine.service;

import com.example.ruleEngine.model.Node;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser {

    private List<String> tokens;  // List to hold the tokens
    private int currentTokenIndex; // Current position in the token list

    public Node parseRule(String ruleString) {
        // Tokenize the input rule
        this.tokens = tokenize(ruleString);
        this.currentTokenIndex = 0;

        // Start parsing from the first token
        return parseExpression();
    }

    // Tokenizer splits the rule into meaningful parts
    private List<String> tokenize(String ruleString) {
        // Regex to capture operators, parentheses, and conditions
        String regex = "\\s*(=>|AND|OR|\\(|\\)|>|<|=|'[a-zA-Z ]*'|[a-zA-Z0-9_]+)\\s*";
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(ruleString);
        while (matcher.find()) {
            tokens.add(matcher.group().trim());
        }

        return tokens;
    }

    // Check if there are more tokens to process
    private boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size();
    }

    // Get the current token without advancing the index
    private String peekToken() {
        return tokens.get(currentTokenIndex);
    }

    // Get the next token and advance the index
    private String nextToken() {
        return tokens.get(currentTokenIndex++);
    }

    // Recursive method to parse an expression
    private Node parseExpression() {
        Node left = parseTerm();  // Parse the left term

        // Process any remaining AND/OR operators at this level
        while (hasMoreTokens() && (peekToken().equals("AND") || peekToken().equals("OR"))) {
            String operator = nextToken();  // Get AND/OR operator
            Node right = parseTerm();       // Parse the right-hand side
            left = new Node(operator, left, right);  // Combine into a new Node
        }

        return left;
    }

    // Method to parse a term (either a condition or a sub-expression)
    private Node parseTerm() {
        String token = nextToken();

        // If we encounter an opening parenthesis, it signifies a sub-expression
        if (token.equals("(")) {
            Node subExpression = parseExpression();  // Recursively parse the sub-expression
            nextToken();  // Consume the closing parenthesis
            return subExpression;
        }

        // Handle conditions like "age > 30" or "salary = 50000"
        String leftOperand = token;
        if (hasMoreTokens() && (peekToken().equals(">") || peekToken().equals("<") || peekToken().equals("="))) {
            String operator = nextToken();  // Get the comparison operator
            String rightOperand = nextToken();  // Get the value on the right side
            return new Node(leftOperand + " " + operator + " " + rightOperand);  // Combine into a single operand node
        }

        // Otherwise, return the token as a single operand
        return new Node(leftOperand);
    }
}