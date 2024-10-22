package com.example.ruleEngine.service;

import com.example.ruleEngine.model.Node;
import com.example.ruleEngine.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleService {

    @Autowired
    private NodeRepository nodeRepository;

    // Method to create a Node from a rule string
    public Node createRule(String ruleString) {
        RuleParser parser = new RuleParser();
        Node rootNode = parser.parseRule(ruleString); // Parsing logic
        return nodeRepository.save(rootNode);  // Persist in MongoDB
    }

    public Node combine(List<Node> ruleASTs){
        // Step 2: Use a heuristic to determine the top-level operator
        CombineRulesService combineRulesService = new CombineRulesService();
        String topOperator = combineRulesService.determineTopOperator(ruleASTs);

        // Step 3: Combine the ASTs using the top-level operator
        Node combinedAST = combineRulesService.combineASTs(ruleASTs, topOperator);

        // Step 4: Save and return the combined AST
        return nodeRepository.save(combinedAST);
    }

    // Combine multiple rules into a single AST
    public Node combineRules(List<String> rules) {
        RuleParser parser = new RuleParser();

        // Step 1: Parse each rule into its own AST
        List<Node> ruleASTs = new ArrayList<>();
        for (String rule : rules) {
            Node ast = parser.parseRule(rule);
            ruleASTs.add(ast);
        }

        return combine(ruleASTs);
    }

    // Method to combine all stored rules
    public Node combineAllRules() {
        // Step 1: Retrieve all rules from the database
        List<Node> allRules = nodeRepository.findAll();
        CombineRulesService combineRulesService = new CombineRulesService();
        // Step 2: Use RuleCombiner to combine all the ASTs

        return combine(allRules);
    }

    // Main function to evaluate the rule based on the given AST and user data
    public boolean evaluateRule(Map<String, Object> ruleAST, Map<String, Object> data) {
        EvaluateRuleService evaluateRuleService = new EvaluateRuleService();
        return evaluateRuleService.evaluateNode(ruleAST, data);
    }
}
