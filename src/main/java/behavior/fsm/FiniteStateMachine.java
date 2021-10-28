package behavior.fsm;

import behavior.Behavior;
import behavior.BehaviorVisitor;

import java.util.LinkedHashSet;
import java.util.Set;

public class FiniteStateMachine implements Behavior {
    private String name;

    private final State startState;
    private final Set<State> states;
    private final Set<Transition> transitions;

    public FiniteStateMachine(String name, State startState) {
        this.name = name;
        this.startState = startState;
        this.states = new LinkedHashSet<>();
        this.transitions = new LinkedHashSet<>();
    }

    public void addTransition(Transition transition) {
        this.states.add(transition.getSource());
        this.states.add(transition.getTarget());
        this.transitions.add(transition);
    }

    public State getStartState() {
        return this.startState;
    }

    public Set<Transition> getTransitions() {
        return new LinkedHashSet<>(this.transitions);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void handle(BehaviorVisitor visitor) {
        visitor.accept(this);
    }
}
