package com.example.ruleEngine;

import com.example.ruleEngine.model.Node;
import com.example.ruleEngine.service.RuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RuleEngineApplicationTests {

	@Autowired
	RuleService ruleService;
	// Sample JSON data representing a user
	private final Map<String, Object> sampleData = Map.of(
			"age", 32,
			"department", "Sales",
			"salary", 60000,
			"experience", 6
	);

	@Test
	public void testCreateComplexRule1() {
		String rule1 = "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)";

		Node expectedASTRule1 = new Node("AND",
				new Node("OR",
						new Node("AND",
								new Node("age > 30"),
								new Node("department = 'Sales'")
						),
						new Node("AND",
								new Node("age < 25"),
								new Node("department = 'Marketing'")
						)
				),
				new Node("OR",
						new Node("salary > 50000"),
						new Node("experience > 5")
				)
		);

		Node nodeAst = ruleService.createRule(rule1);
		assertNotNull(nodeAst.getId(), "Rule ID should not be null after creation");
		assertEquals(expectedASTRule1, nodeAst, "AST for rule1 should match the expected structure");
	}

	@Test
	public void testCreateComplexRule2() {
		String rule2 = "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)";

		Node expectedASTRule2 = new Node("AND",
				new Node("AND",
						new Node("age > 30"),
						new Node("department = 'Marketing'")
				),
				new Node("OR",
						new Node("salary > 20000"),
						new Node("experience > 5")
				)
		);

		Node nodeAst = ruleService.createRule(rule2);
		assertNotNull(nodeAst.getId(), "Rule ID should not be null after creation");
		assertEquals(expectedASTRule2, nodeAst, "AST for rule1 should match the expected structure");
	}

	@Test
	public void testCombineRules() {
		String rule1 = "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)";
		String rule2 = "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)";
		List<String> rules = new ArrayList<>();
		rules.add(rule1);
		rules.add(rule2);

		Node combinedAst = ruleService.combineRules(rules);

		Node expectedCombinedAST = new Node("AND",
				new Node("AND",
						new Node("OR",
								new Node("AND",
										new Node("age > 30"),
										new Node("department = 'Sales'")
								),
								new Node("AND",
										new Node("age < 25"),
										new Node("department = 'Marketing'")
								)
						),
						new Node("OR",
								new Node("salary > 50000"),
								new Node("experience > 5")
						)
				),
				new Node("AND",
						new Node("AND",
								new Node("age > 30"),
								new Node("department = 'Marketing'")
						),
						new Node("OR",
								new Node("salary > 20000"),
								new Node("experience > 5")
						)
				)
		);

		assertEquals(combinedAst, expectedCombinedAST, "Combined AST should match the expected structure");
	}

	@Test
	public void testEvaluateRule_AgeAndDepartment() {
		// Rule: (age > 30 AND department = 'Sales')
		Map<String, Object> ruleAst = Map.of(
				"type", "OPERATOR",
				"operator", "AND",
				"left", Map.of("type", "OPERAND", "value", "age > 30"),
				"right", Map.of("type", "OPERAND", "value", "department = 'Sales'")
		);

		boolean result = ruleService.evaluateRule(ruleAst, sampleData);

		assertTrue(result, "Rule should evaluate to true for age > 30 and department = 'Sales'");
	}

	@Test
	public void testEvaluateRule_SalaryOrExperience() {
		// Rule: (salary > 50000 OR experience > 5)
		Map<String, Object> ruleAst = Map.of(
				"type", "OPERATOR",
				"operator", "OR",
				"left", Map.of("type", "OPERAND", "value", "salary > 50000"),
				"right", Map.of("type", "OPERAND", "value", "experience > 5")
		);

		boolean result = ruleService.evaluateRule(ruleAst, sampleData);

		assertTrue(result, "Rule should evaluate to true for salary > 50000 or experience > 5");
	}

	@Test
	public void testEvaluateRule_ComplexRule() {
		// Rule: (age > 30 AND department = 'Marketing') AND (salary > 20000 OR experience > 5)
		Map<String, Object> ruleAst = Map.of(
				"type", "OPERATOR",
				"operator", "AND",
				"left", Map.of(
						"type", "OPERATOR",
						"operator", "AND",
						"left", Map.of("type", "OPERAND", "value", "age > 30"),
						"right", Map.of("type", "OPERAND", "value", "department = 'Marketing'")
				),
				"right", Map.of(
						"type", "OPERATOR",
						"operator", "OR",
						"left", Map.of("type", "OPERAND", "value", "salary > 20000"),
						"right", Map.of("type", "OPERAND", "value", "experience > 5")
				)
		);

		boolean result = ruleService.evaluateRule(ruleAst, sampleData);

		assertFalse(result, "Rule should evaluate to false for age > 30 and department = 'Marketing' with salary > 20000 or experience > 5");
	}


}
