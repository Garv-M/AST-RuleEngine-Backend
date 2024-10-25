package com.example.ruleEngine.controller;

import com.example.ruleEngine.model.Node;
import com.example.ruleEngine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/create")
    public Node createRule(@RequestBody Map<String, String> requestBody) {
        String ruleString = requestBody.get("rule");
        return ruleService.createRule(ruleString);
    }

    @PostMapping("/combine")
    public Node combineRules(@RequestBody Map<String, List<String>> requestBody) {
        List<String> rules = requestBody.get("rules");
        return ruleService.combineRules(rules);
    }

    @PostMapping("/evaluate")
    public boolean evaluateRule(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> ruleAST = (Map<String, Object>) requestBody.get("ruleAST");
        Map<String, Object> userData = (Map<String, Object>) requestBody.get("userData");

        return ruleService.evaluateRule(ruleAST, userData);
    }

    @GetMapping("/{ruleId}/view")
    public Map<String, String> viewRuleAsText(@PathVariable String ruleId) {
        Node ruleAst = ruleService.getRuleById(ruleId);
        String ruleString = ruleService.astToString(ruleAst);
        return Map.of("rule",ruleString);
    }

    @PostMapping("/{ruleId}/update")
    public ResponseEntity<Map<String, String>> updateRuleFromText(@PathVariable String ruleId, @RequestBody Map<String, String> request) {
        String modifiedRule = request.get("rule");

        if (modifiedRule == null || modifiedRule.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Rule string cannot be empty.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Node updatedAst = ruleService.UpdateRule(modifiedRule, ruleId);
        String updatedRuleString = ruleService.astToString(updatedAst);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rule updated successfully");
        response.put("updatedRule", updatedRuleString);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allRules")
    public List<Map<String, String>> getAllRules() {
        return ruleService.getAllRulesAsString();
    }
}
