package behavior.bpmn;

import behavior.Behavior;
import behavior.BehaviorVisitor;

import java.util.Set;

public class BPMNCollaboration implements Behavior {
    private final String name;
    private final Set<Process> participants;
    private final Set<MessageFlow> messageFlows;

    public BPMNCollaboration(String name, Set<Process> participants, Set<MessageFlow> messageFlows) {
        this.name = name;
        this.participants = participants;
        this.messageFlows = messageFlows;
    }

    public Set<Process> getParticipants() {
        return participants;
    }

    public Set<MessageFlow> getMessageFlows() {
        return messageFlows;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(BehaviorVisitor visitor) {
        visitor.handle(this);
    }
}
