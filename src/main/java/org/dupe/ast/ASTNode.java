package org.dupe.ast;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

public class ASTNode {

    public static ASTNode literal(@Nullable Long value) {
        return new ASTNode(TokenType.LITERAL, value, null, null, null);
    }

    public static ASTNode operator(TokenType type, ASTNode left, ASTNode right) {
        return new ASTNode(type, null, null, left, right);
    }

    public static ASTNode builtinFn(String fnName, ASTNode left, ASTNode right) {
        return new ASTNode(TokenType.BUILTIN_FN, null, fnName, left, right);
    }

    public static ASTNode openParen() {
        return new ASTNode(TokenType.OPEN_PAREN, null, null, null, null);
    }

    private final TokenType type;

    /**
     * Holds the value of literal expressions, null for non-literal expressions
     */
    @Nullable
    private final Long value;
    /**
     * Holds the name of built-in functions, null for all other expressions
     */
    @Nullable
    private final String fnName;

    // TODO: Assumes binary operators/functions. Expand to handle unary and multi-arg
    @Nullable
    private final ASTNode leftSubExpr;
    @Nullable
    private final ASTNode rightSubExpr;

    private final List<ASTNode> subExprs;

    // Private constructor: use static factory methods instead
    private ASTNode(
            TokenType type,
            @Nullable Long value,
            @Nullable String fnName,
            @Nullable ASTNode leftSubExpr,
            @Nullable ASTNode rightSubExpr) {
        this.type = type;
        this.value = value;
        this.fnName = fnName;
        this.leftSubExpr = leftSubExpr;
        this.rightSubExpr = rightSubExpr;
        this.subExprs = List.of();
    }

    private ASTNode(
            TokenType type,
            @Nullable Long value,
            @Nullable String fnName,
            List<ASTNode> subExprs) {
        this.type = type;
        this.value = value;
        this.fnName = fnName;
        this.leftSubExpr = null;
        this.rightSubExpr = null;
        this.subExprs = subExprs;
    }

    public boolean isLiteral() {
        return type == TokenType.LITERAL;
    }

    public TokenType getType() {
        return type;
    }

    public Optional<Long> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<String> getFnName() {
        return Optional.ofNullable(fnName);
    }

    public Optional<ASTNode> getLeftSubExpr() {
        return Optional.ofNullable(leftSubExpr);
    }

    public Optional<ASTNode> getRightSubExpr() {
        return Optional.ofNullable(rightSubExpr);
    }

    public ASTNode withSubExprs(ASTNode left, ASTNode right) {
        return new ASTNode(type, value, fnName, left, right);
    }

    public ASTNode withSubExprs(List<ASTNode> subExprs) {
        return new ASTNode(type, value, fnName, subExprs);
    }

    public String toString() {
        return toStringHelper(this, 1);
    }

    private static String toStringHelper(@Nullable ASTNode node, int indentLevel) {
        if (node == null) {
            return "";

        }

        String indentation = createIndentationString(indentLevel);
        String left = toStringHelper(node.leftSubExpr, indentLevel + 1);
        left = left.isEmpty() ? "None" : indentation + left;
        String right = toStringHelper(node.rightSubExpr, indentLevel + 1);
        right = right.isEmpty() ? "None" : indentation + right;

        return String.format("( ASTNode{ TokenType: %s, literal: %s, fn: %s, %s, %s } )",
                node.type,
                node.value,
                node.fnName,
                left,
                right);
    }

    private static String createIndentationString(int indentLevel) {
        String indentation = "\n";
        for(int i = 0; i < indentLevel; i++) {
            indentation += "    ";
        }
        return indentation;
    }
}

