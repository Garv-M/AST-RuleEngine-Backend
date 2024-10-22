package com.example.ruleEngine.controller;

import com.example.ruleEngine.model.Node;
import com.example.ruleEngine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    // POST endpoint to create a rule
    @PostMapping("/create")
    public Node createRule(@RequestBody Map<String, String> requestBody) {
        String ruleString = requestBody.get("rule");
        return ruleService.createRule(ruleString);
    }

    // POST endpoint to combine multiple rules
    @PostMapping("/combine")
    public Node combineRules(@RequestBody Map<String, List<String>> requestBody) {
        List<String> rules = requestBody.get("rules");  // Extract the list of rule strings
        return ruleService.combineRules(rules);         // Combine the rules into an AST
    }

    // GET endpoint to combine all created rules
    @GetMapping("/combine/all")
    public Node combineAllRules() {
        return ruleService.combineAllRules();
    }

    // POST endpoint to evaluate the rule
    @PostMapping("/evaluate")
    public boolean evaluateRule(@RequestBody Map<String, Object> requestBody) {
        // Extract the rule AST and the user data from the request body
        Map<String, Object> ruleAST = (Map<String, Object>) requestBody.get("ruleAST");
        Map<String, Object> userData = (Map<String, Object>) requestBody.get("userData");

        // Call the RuleService to evaluate the rule
        return ruleService.evaluateRule(ruleAST, userData);
    }
}
