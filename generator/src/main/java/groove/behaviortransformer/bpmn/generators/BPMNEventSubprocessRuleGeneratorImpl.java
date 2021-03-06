package groove.behaviortransformer.bpmn.generators;

import behavior.bpmn.AbstractProcess;
import behavior.bpmn.BPMNCollaboration;
import behavior.bpmn.EventSubprocess;
import behavior.bpmn.MessageFlow;
import behavior.bpmn.auxiliary.exceptions.BPMNRuntimeException;
import behavior.bpmn.events.StartEvent;
import groove.behaviortransformer.bpmn.BPMNRuleGenerator;
import groove.behaviortransformer.bpmn.BPMNToGrooveTransformerHelper;
import groove.graph.GrooveNode;
import groove.graph.rule.GrooveRuleBuilder;

import java.util.Set;

import static groove.behaviortransformer.GrooveTransformerHelper.createStringNodeLabel;
import static groove.behaviortransformer.bpmn.BPMNToGrooveTransformerConstants.*;

public class BPMNEventSubprocessRuleGeneratorImpl implements BPMNEventSubprocessRuleGenerator {
    private final BPMNRuleGenerator bpmnRuleGenerator;
    private final BPMNCollaboration collaboration;
    private final GrooveRuleBuilder ruleBuilder;
    private boolean useSFId;

    public BPMNEventSubprocessRuleGeneratorImpl(BPMNRuleGenerator bpmnRuleGenerator,
                                                GrooveRuleBuilder ruleBuilder,
                                                boolean useSFId) {
        this.bpmnRuleGenerator = bpmnRuleGenerator;
        this.collaboration = bpmnRuleGenerator.getCollaboration();
        this.ruleBuilder = ruleBuilder;
        this.useSFId = useSFId;
    }

    @Override
    public void generateRulesForEventSubprocesses(AbstractProcess process) {
        process.getEventSubprocesses().forEach(eventSubprocess -> this.generateRulesForEventSubprocess(process,
                                                                                                       eventSubprocess));
    }


    private void generateRulesForEventSubprocess(AbstractProcess process, EventSubprocess eventSubprocess) {
        // Start event rule generation is special
        generateRulesForStartEvents(process, eventSubprocess, collaboration, ruleBuilder);
        // Standard rule generation for other elements.
        bpmnRuleGenerator.generateRulesForProcess(eventSubprocess);
        // Termination rule
        generateTerminateEventSubProcessRule(process, eventSubprocess, ruleBuilder);
    }

    private void generateTerminateEventSubProcessRule(AbstractProcess parentProcess,
                                                      EventSubprocess eventSubprocess,
                                                      GrooveRuleBuilder ruleBuilder) {
        String eSubprocessName = eventSubprocess.getName();
        ruleBuilder.startRule(eSubprocessName + END);
        GrooveNode parentProcessInstance = BPMNToGrooveTransformerHelper.contextProcessInstance(parentProcess,
                                                                                                ruleBuilder);
        bpmnRuleGenerator.deleteTerminatedSubprocess(ruleBuilder, eSubprocessName, parentProcessInstance);
        ruleBuilder.buildRule();
    }


    private void generateRulesForStartEvents(AbstractProcess process,
                                             EventSubprocess eventSubprocess,
                                             BPMNCollaboration collaboration,
                                             GrooveRuleBuilder ruleBuilder) {
        eventSubprocess.getStartEvents().forEach(startEvent -> {
            switch (startEvent.getType()) {
                case NONE:
                    throw new BPMNRuntimeException("None start events in event subprocesses are useless!");
                case MESSAGE:
                    createStartInterruptingEvenSubprocessFromMessageRules(process,
                                                                          eventSubprocess,
                                                                          collaboration,
                                                                          ruleBuilder,
                                                                          startEvent);
                    break;
                case MESSAGE_NON_INTERRUPTING:
                    createStartNonInterruptingEvenSubprocessFromMessageRules(process,
                                                                             eventSubprocess,
                                                                             collaboration,
                                                                             ruleBuilder,
                                                                             startEvent);

                    break;
                case SIGNAL:
                    // Implemented in the throw part.
                case SIGNAL_NON_INTERRUPTING:
                    // Implemented in the throw part.
                    break;
                default:
                    throw new BPMNRuntimeException("Unexpected start event type encountered: " + startEvent.getType());
            }
        });
    }

    private void createStartInterruptingEvenSubprocessFromMessageRules(AbstractProcess parentProcess,
                                                                       EventSubprocess eventSubprocess,
                                                                       BPMNCollaboration collaboration,
                                                                       GrooveRuleBuilder ruleBuilder,
                                                                       StartEvent startEvent) {
        Set<MessageFlow> incomingMessageFlows = collaboration.getIncomingMessageFlows(startEvent);
        incomingMessageFlows.forEach(incomingMessageFlow -> {
            ruleBuilder.startRule(getMessageStartEventRuleName(incomingMessageFlows, incomingMessageFlow, startEvent));
            GrooveNode parentProcessInstance = createMessageStartEventRulePart(parentProcess,
                                                                               ruleBuilder,
                                                                               eventSubprocess,
                                                                               incomingMessageFlow,
                                                                               startEvent);
            // The parent is interrupted, i.e., all its tokens are deleted.
            BPMNToGrooveTransformerHelper.deleteAllTokensForProcess(ruleBuilder, parentProcessInstance);
            ruleBuilder.buildRule();
        });

    }

    private GrooveNode createMessageStartEventRulePart(AbstractProcess parentProcess,
                                                       GrooveRuleBuilder ruleBuilder,
                                                       EventSubprocess eventSubprocess,
                                                       MessageFlow incomingMessageFlow,
                                                       StartEvent startEvent) {
        // Needs a running parent process
        GrooveNode parentProcessInstance = BPMNToGrooveTransformerHelper.contextProcessInstance(parentProcess,
                                                                                                ruleBuilder);

        // Start new subprocess instance of process
        GrooveNode eventSubProcessInstance = BPMNToGrooveTransformerHelper.addProcessInstance(ruleBuilder,
                                                                                              eventSubprocess.getName());
        ruleBuilder.addEdge(SUBPROCESS, parentProcessInstance, eventSubProcessInstance);

        // Consumes the message
        GrooveNode message = ruleBuilder.deleteNode(TYPE_MESSAGE);
        ruleBuilder.deleteEdge(POSITION,
                               message,
                               ruleBuilder.contextNode(createStringNodeLabel(incomingMessageFlow.getName())));

        // Spawns a new token at each outgoing flow.
        BPMNToGrooveTransformerHelper.addOutgoingTokensForFlowNodeToProcessInstance(startEvent,
                                                                                    ruleBuilder,
                                                                                    eventSubProcessInstance,
                                                                                    useSFId);
        return parentProcessInstance;
    }

    private String getMessageStartEventRuleName(Set<MessageFlow> incomingMessageFlows,
                                                MessageFlow incomingMessageFlow,
                                                StartEvent startEvent) {
        return incomingMessageFlows.size() > 1 ? incomingMessageFlow.getName() : startEvent.getName();
    }

    private void createStartNonInterruptingEvenSubprocessFromMessageRules(AbstractProcess process,
                                                                          EventSubprocess eventSubprocess,
                                                                          BPMNCollaboration collaboration,
                                                                          GrooveRuleBuilder ruleBuilder,
                                                                          StartEvent startEvent) {
        Set<MessageFlow> incomingMessageFlows = collaboration.getIncomingMessageFlows(startEvent);
        incomingMessageFlows.forEach(incomingMessageFlow -> {
            ruleBuilder.startRule(getMessageStartEventRuleName(incomingMessageFlows, incomingMessageFlow, startEvent));
            createMessageStartEventRulePart(process, ruleBuilder, eventSubprocess, incomingMessageFlow, startEvent);
            ruleBuilder.buildRule();
        });
    }
}
