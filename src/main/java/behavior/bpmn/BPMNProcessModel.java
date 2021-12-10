package behavior.bpmn;

import behavior.Behavior;
import behavior.BehaviorVisitor;

import java.util.Collections;
import java.util.Set;

public class BPMNProcessModel implements Behavior {
    private String name;
    private final StartEvent startEvent;
    private final Set<EndEvent> endEvents;
    private final Set<SequenceFlow> sequenceFlows;

    public BPMNProcessModel(String name, StartEvent startEvent, Set<EndEvent> endEvents, Set<SequenceFlow> sequenceFlows) {
        this.name = name;
        this.startEvent = startEvent;
        this.endEvents = endEvents;
        this.sequenceFlows = sequenceFlows;
    }

    public StartEvent getStartEvent() {
        return this.startEvent;
    }

    public Set<SequenceFlow> getSequenceFlows() {
        return Collections.unmodifiableSet(this.sequenceFlows);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void accept(BehaviorVisitor visitor) {
        visitor.handle(this);
    }
}
