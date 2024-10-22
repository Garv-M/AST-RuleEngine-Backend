package com.example.ruleEngine.service;

import com.example.ruleEngine.model.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombineRulesService {
    // Method to determine the most frequent operator in the rule set
    public String determineTopOperator(List<Node> rules) {
        Map<String, Integer> operatorCount = new HashMap<>();
        operatorCount.put("AND", 0);
        operatorCount.put("OR", 0);

        for (Node rule : rules) {
            if (rule.getOperator() != null) {
                operatorCount.put(rule.getOperator(), operatorCount.get(rule.getOperator()) + 1);
            }
        }

        // Return the most frequent operator (AND or OR)
        return operatorCount.get("AND") >= operatorCount.get("OR") ? "AND" : "OR";
    }

    // Combine individual ASTs under a top-level operator node
    public Node combineASTs(List<Node> asts, String operator) {
        if (asts.size() == 1) {
            Node root = asts.get(0);
            clearNodeIds(root); // Recursively clear the ID for the entire tree
            return root;
        }

        Node root = asts.get(0);
        clearNodeIds(root); // Recursively clear the ID for the entire tree

        for (int i = 1; i < asts.size(); i++) {
            Node newRoot = new Node(operator, root, asts.get(i));
            clearNodeIds(newRoot); // Recursively clear the ID for the new combined node and its subtree
            root = newRoot;
        }

        return root;
    }

    // Helper method to recursively clear IDs from nodes
    private void clearNodeIds(Node node) {
        if (node == null) {
            return;
        }

        // Clear the ID of the current node
        node.setId(null);

        // Recursively clear IDs for the left and right children
        clearNodeIds(node.getLeft());
        clearNodeIds(node.getRight());
    }
}
