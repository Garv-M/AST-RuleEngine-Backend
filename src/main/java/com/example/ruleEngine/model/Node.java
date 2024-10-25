package com.example.ruleEngine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "rules")
public class Node {
    @Id
    private String id;

    public enum NodeType {
        OPERATOR,
        OPERAND
    }

    private NodeType type;
    private Node left;
    private Node right;
    private String operator;
    private String value;

    public Node() {
    }

    public Node(String value) {
        this.type = NodeType.OPERAND;
        this.value = value;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return type == node.type &&
                Objects.equals(left, node.left) &&
                Objects.equals(right, node.right) &&
                Objects.equals(operator, node.operator) &&
                Objects.equals(value, node.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, left, right, operator, value);
    }
}
