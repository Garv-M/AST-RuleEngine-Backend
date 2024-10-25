package com.example.ruleEngine.service;

import com.example.ruleEngine.model.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombineRulesService {
    public String determineTopOperator(List<Node> rules) {
        Map<String, Integer> operatorCount = new HashMap<>();
        operatorCount.put("AND", 0);
        operatorCount.put("OR", 0);

        for (Node rule : rules) {
            if (rule.getOperator() != null) {
                operatorCount.put(rule.getOperator(), operatorCount.get(rule.getOperator()) + 1);
            }
        }
        return operatorCount.get("AND") >= operatorCount.get("OR") ? "AND" : "OR";
    }

    public Node combineASTs(List<Node> asts, String operator) {
        if (asts.size() == 1) {
            Node root = asts.get(0);
            clearNodeIds(root);
            return root;
        }

        Node root = asts.get(0);
        clearNodeIds(root);

        for (int i = 1; i < asts.size(); i++) {
            Node newRoot = new Node(operator, root, asts.get(i));
            clearNodeIds(newRoot);
            root = newRoot;
        }

        return root;
    }

    private void clearNodeIds(Node node) {
        if (node == null) {
            return;
        }
        node.setId(null);

        clearNodeIds(node.getLeft());
        clearNodeIds(node.getRight());
    }
}
