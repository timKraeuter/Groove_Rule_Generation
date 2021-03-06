package behavior.bpmn.auxiliary;

import behavior.bpmn.EventSubprocess;
import behavior.bpmn.FlowNode;
import behavior.bpmn.SequenceFlow;
import behavior.bpmn.events.StartEvent;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BPMNEventSubprocessBuilder implements BPMNModelBuilder {
    private final Set<SequenceFlow> sequenceFlows;
    private final Set<FlowNode> flowNodes;
    private final Set<EventSubprocess> eventSubprocesses;
    private String name;

    public BPMNEventSubprocessBuilder() {
        this.sequenceFlows = new LinkedHashSet<>();
        flowNodes = new LinkedHashSet<>();
        eventSubprocesses = new LinkedHashSet<>();
    }

    public BPMNEventSubprocessBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BPMNEventSubprocessBuilder sequenceFlow(String id, String name, FlowNode from, FlowNode to) {
        final SequenceFlow sequenceFlow = new SequenceFlow(id, name, from, to);
        this.sequenceFlows.add(sequenceFlow);
        from.addOutgoingSequenceFlow(sequenceFlow);
        to.addIncomingSequenceFlow(sequenceFlow);
        return this;
    }

    public BPMNEventSubprocessBuilder sequenceFlow(String id, FlowNode from, FlowNode to) {
        return sequenceFlow(id, "", from, to);
    }

    @Override
    public BPMNModelBuilder flowNode(FlowNode flowNode) {
        flowNodes.add(flowNode);
        return this;
    }

    @Override
    public BPMNEventSubprocessBuilder eventSubprocess(EventSubprocess eventSubprocess) {
        eventSubprocesses.add(eventSubprocess);
        return this;
    }

    @Override
    public Set<StartEvent> getStartEvents() {
        return new HashSet<>();
    }

    @Override
    public BPMNEventSubprocessBuilder startEvent(StartEvent startEvent) {
        // NOOP since it is not needed.
        return this;
    }

    public EventSubprocess build() {
        return new EventSubprocess(name, sequenceFlows, flowNodes, eventSubprocesses);
    }
}
