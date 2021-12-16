package behavior.activity.nodes;

import behavior.activity.expression.Expression;

import java.util.List;

/**
 * Executable Activity Node consisting of one opaque action.
 */
public class OpaqueAction extends ActivityNode {
    private final List<Expression> expressions;

    public OpaqueAction(String name, List<Expression> expressions) {
        super(name);
        this.expressions = expressions;
    }
}