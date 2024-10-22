package com.example.ruleEngine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rules")
public class Node {
    @Id
    private String id;  // MongoDB will use this ID

    public enum NodeType {
        OPERATOR,
        OPERAND
    }

    private NodeType type;
    private Node left;
    private Node right;
    private String operator;
    private String value;

    // Default constructor (no-argument constructor)
    public Node() {
    }

    // Constructors, getters, and setters remain the same
    // Constructor for Operand Nodes (conditions as String)
    public Node(String value) {
        this.type = NodeType.OPERAND;
        this.value = value;
    }

    // Constructor for Operator Nodes (e.g., AND, OR)
    public Node(String operator, Node left, Node right) {
        this.type = NodeType.OPERATOR;
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
