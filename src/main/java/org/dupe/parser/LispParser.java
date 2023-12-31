package org.dupe.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dupe.ast.ASTNode;
import org.dupe.ast.TokenType;

/**
 * Parses Lisp expressions into an AST
 *
 * <p>A brief Lisp language overview:
 *  {@link https://courses.cs.vt.edu/~cs1104/TowerOfBabel/LISP/Lisp.outline.html}
 */
public final class LispParser {

    private static final String OPEN_EXPR = "(";
    private static final String CLOSE_EXPR = ")";

    // TODO: Add error handling for invalid expressions
    //       For now, we assume the given expression is well-formed
    public static ASTNode parse(String expr) {
        var tokens = tokenize(expr);

        Stack<ASTNode> nodes = new Stack<>();
        for (String token : tokens) {
            if (token.equals(OPEN_EXPR)) {
                nodes.push(ASTNode.openParen());
                continue;
            }

            // TODO: Change this to handle unary or multi argument operators
            // IDEA: Have a token type for open parens, when closed paren is seen
            // pop off the stack until the open paren is seen, the node right before
            // the open paren is the root node, and all the other nodes popped
            // off are the children
            // We need this approach because the first tokens after an open paren
            // may be another paren encosed expression
            if (token.equals(CLOSE_EXPR)) {
                ASTNode curr = nodes.pop();
                List<ASTNode> args = new ArrayList<>();
                while (curr.getType() != TokenType.OPEN_PAREN) {
                    args.add(curr);
                    curr = nodes.pop();
                }
                ASTNode root = args.isEmpty() ? curr : args.remove(0);
                nodes.push(root.withSubExprs(args));
                continue;
            }

            var type = TokenType.from(token);
            if (type == TokenType.LITERAL) {
                nodes.push(ASTNode.literal(Long.parseLong(token)));
            } else if (type == TokenType.BUILTIN_FN) {
                nodes.push(ASTNode.builtinFn(token, null, null));
            } else {
                nodes.push(ASTNode.operator(type, null, null));
            }
        }

        return nodes.pop();
   }

   private static String[] tokenize(String expr) {
        // Expand parentheses with spaces so they can be split as their own tokens
        var parenExpanded = expr
            .replace("(", " ( ")
            .replace(")", " ) ");
        var trimmed = parenExpanded.strip();
        return trimmed.split("\\s+");
   }
}
