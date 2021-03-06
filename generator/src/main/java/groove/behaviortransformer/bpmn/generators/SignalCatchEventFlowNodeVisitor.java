package groove.behaviortransformer.bpmn.generators;


import behavior.bpmn.Process;
import behavior.bpmn.activities.Activity;
import behavior.bpmn.activities.CallActivity;
import behavior.bpmn.activities.tasks.ReceiveTask;
import behavior.bpmn.activities.tasks.SendTask;
import behavior.bpmn.activities.tasks.Task;
import behavior.bpmn.auxiliary.FlowNodeVisitor;
import behavior.bpmn.events.*;
import behavior.bpmn.gateways.EventBasedGateway;
import behavior.bpmn.gateways.ExclusiveGateway;
import behavior.bpmn.gateways.InclusiveGateway;
import behavior.bpmn.gateways.ParallelGateway;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

class SignalCatchEventFlowNodeVisitor implements FlowNodeVisitor {
    private final BPMNEventRuleGeneratorImpl bpmnEventRuleGenerator;
    private final EventDefinition eventDefinition;
    private final Set<Event> signalCatchEvents;
    private final Set<BoundaryEvent> signalBoundaryCatchEvents;
    private final Set<Process> seenProcesses;

    public SignalCatchEventFlowNodeVisitor(BPMNEventRuleGeneratorImpl bpmnEventRuleGenerator,
                                           EventDefinition eventDefinition,
                                           Set<Event> signalCatchEvents,
                                           Set<BoundaryEvent> signalBoundaryCatchEvents,
                                           Set<Process> seenProcesses) {
        this.bpmnEventRuleGenerator = bpmnEventRuleGenerator;
        this.eventDefinition = eventDefinition;
        this.signalCatchEvents = signalCatchEvents;
        this.signalBoundaryCatchEvents = signalBoundaryCatchEvents;
        this.seenProcesses = seenProcesses;
    }

    @Override
    public void handle(Task task) {
        addPotentialBoundaryEvents(task);
    }

    private void addPotentialBoundaryEvents(Activity task) {
        task.getBoundaryEvents().stream().filter(boundaryEvent -> boundaryEvent.getType() ==
                                                                  BoundaryEventType.SIGNAL).forEach(
                signalBoundaryCatchEvents::add);
    }

    @Override
    public void handle(SendTask task) {
        addPotentialBoundaryEvents(task);
    }

    @Override
    public void handle(ReceiveTask task) {
        addPotentialBoundaryEvents(task);

    }

    @Override
    public void handle(CallActivity callActivity) {
        Pair<Set<Event>, Set<BoundaryEvent>> signalAndBoundarySignalEvents =
                bpmnEventRuleGenerator.findAllCorrespondingSignalCatchEvents(
                callActivity.getSubProcessModel(),
                eventDefinition,
                seenProcesses);
        signalCatchEvents.addAll(signalAndBoundarySignalEvents.getLeft());
        signalBoundaryCatchEvents.addAll(signalAndBoundarySignalEvents.getRight());

        addPotentialBoundaryEvents(callActivity);
    }

    @Override
    public void handle(ExclusiveGateway exclusiveGateway) {
        // not relevant
    }

    @Override
    public void handle(ParallelGateway parallelGateway) {
        // not relevant
    }

    @Override
    public void handle(InclusiveGateway inclusiveGateway) {
        // not relevant
    }

    @Override
    public void handle(StartEvent startEvent) {
        if ((startEvent.getType() == StartEventType.SIGNAL ||
             startEvent.getType() == StartEventType.SIGNAL_NON_INTERRUPTING) &&
            startEvent.getEventDefinition().getGlobalSignalName().equals(eventDefinition.getGlobalSignalName())) {
            signalCatchEvents.add(startEvent);
        }
    }

    @Override
    public void handle(IntermediateThrowEvent intermediateThrowEvent) {
        // not relevant
    }

    @Override
    public void handle(IntermediateCatchEvent intermediateCatchEvent) {
        if (intermediateCatchEvent.getType() == IntermediateCatchEventType.SIGNAL &&
            intermediateCatchEvent.getEventDefinition().getGlobalSignalName().equals(eventDefinition.getGlobalSignalName())) {
            signalCatchEvents.add(intermediateCatchEvent);
        }
    }

    @Override
    public void handle(EndEvent endEvent) {
        // not relevant
    }

    @Override
    public void handle(EventBasedGateway eventBasedGateway) {
        // not relevant
    }
}
