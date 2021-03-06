package groove.behaviortransformer.bpmn;

import behavior.bpmn.Process;
import behavior.bpmn.*;
import behavior.bpmn.activities.CallActivity;
import behavior.bpmn.auxiliary.exceptions.BPMNRuntimeException;
import behavior.bpmn.events.StartEvent;
import groove.behaviortransformer.GrooveTransformer;
import groove.graph.GrooveNode;
import groove.graph.rule.GrooveRuleBuilder;

import static groove.behaviortransformer.GrooveTransformer.*;
import static groove.behaviortransformer.GrooveTransformerHelper.createStringNodeLabel;
import static groove.behaviortransformer.bpmn.BPMNToGrooveTransformerConstants.*;

public class BPMNToGrooveTransformerHelper {

    private BPMNToGrooveTransformerHelper() {
        // Only helper class with static methods.
    }

    static void updateTokenPositionForProcessInstance(String oldPosition,
                                                      String newPosition,
                                                      GrooveRuleBuilder ruleBuilder,
                                                      GrooveNode processInstance) {
        GrooveNode token = ruleBuilder.contextNode(TYPE_TOKEN);
        ruleBuilder.contextEdge(TOKENS, processInstance, token);
        GrooveNode oldTokenPosition = ruleBuilder.contextNode(createStringNodeLabel(oldPosition));
        ruleBuilder.deleteEdge(POSITION, token, oldTokenPosition);

        GrooveNode newTokenPosition = ruleBuilder.contextNode(createStringNodeLabel(newPosition));
        ruleBuilder.addEdge(POSITION, token, newTokenPosition);
    }

    public static void updateTokenPositionWhenRunning(AbstractProcess process,
                                                      String oldPosition,
                                                      String newPosition,
                                                      GrooveRuleBuilder ruleBuilder) {
        // Process instance has to be running
        GrooveNode processInstance = contextProcessInstance(process, ruleBuilder);

        // Update tokens
        updateTokenPositionForProcessInstance(oldPosition, newPosition, ruleBuilder, processInstance);
    }

    public static GrooveNode contextProcessInstance(AbstractProcess process, GrooveRuleBuilder ruleBuilder) {
        GrooveNode processInstance = contextProcessInstanceWithOnlyName(process, ruleBuilder);
        GrooveNode running = ruleBuilder.contextNode(TYPE_RUNNING);
        ruleBuilder.contextEdge(STATE, processInstance, running);
        return processInstance;
    }

    public static GrooveNode contextProcessInstanceWithQuantifier(AbstractProcess process,
                                                                  GrooveRuleBuilder ruleBuilder,
                                                                  GrooveNode quantifier) {
        GrooveNode processInstance;
        processInstance = contextProcessInstanceWithOnlyName(process, ruleBuilder);
        GrooveNode running = ruleBuilder.contextNode(TYPE_RUNNING);
        ruleBuilder.contextEdge(STATE, processInstance, running);
        ruleBuilder.contextEdge(GrooveTransformer.AT, processInstance, quantifier);
        ruleBuilder.contextEdge(GrooveTransformer.AT, running, quantifier);
        return processInstance;
    }

    public static GrooveNode addProcessInstance(GrooveRuleBuilder ruleBuilder, String processName) {
        GrooveNode processInstance = addProcessInstanceWithName(ruleBuilder, processName);
        ruleBuilder.addEdge(STATE, processInstance, ruleBuilder.addNode(TYPE_RUNNING));
        return processInstance;
    }

    private static GrooveNode addProcessInstanceWithName(GrooveRuleBuilder ruleBuilder, String processName) {
        GrooveNode processInstance = ruleBuilder.addNode(TYPE_PROCESS_SNAPSHOT);
        ruleBuilder.addEdge(NAME, processInstance, ruleBuilder.contextNode(createStringNodeLabel(processName)));
        return processInstance;
    }

    public static GrooveNode addProcessInstanceWithQuantifier(GrooveRuleBuilder ruleBuilder,
                                                              String processName,
                                                              GrooveNode quantifier) {
        GrooveNode processInstance = addProcessInstanceWithName(ruleBuilder, processName);
        GrooveNode running = ruleBuilder.addNode(TYPE_RUNNING);
        ruleBuilder.addEdge(STATE, processInstance, running);
        ruleBuilder.contextEdge(GrooveTransformer.AT, processInstance, quantifier);
        ruleBuilder.contextEdge(GrooveTransformer.AT, running, quantifier);
        return processInstance;
    }

    public static GrooveNode contextProcessInstanceWithOnlyName(AbstractProcess process,
                                                                GrooveRuleBuilder ruleBuilder) {
        GrooveNode processInstance = ruleBuilder.contextNode(TYPE_PROCESS_SNAPSHOT);
        ruleBuilder.contextEdge(NAME,
                                processInstance,
                                ruleBuilder.contextNode(createStringNodeLabel(process.getName())));
        return processInstance;
    }

    public static void addOutgoingTokensForFlowNodeToProcessInstance(FlowNode flowNode,
                                                                     GrooveRuleBuilder ruleBuilder,
                                                                     GrooveNode processInstance,
                                                                     boolean useSFId) {
        flowNode.getOutgoingFlows().forEach(sequenceFlow -> addTokenWithPosition(ruleBuilder,
                                                                                 processInstance,
                                                                                 getSequenceFlowIdOrDescriptiveName(
                                                                                         sequenceFlow,
                                                                                         useSFId)));
    }

    public static void addOutgoingTokensForFlowNodeToProcessInstanceWithQuantifier(FlowNode flowNode,
                                                                                   GrooveRuleBuilder ruleBuilder,
                                                                                   GrooveNode processInstance,
                                                                                   GrooveNode quantifier,
                                                                                   boolean useSFId) {
        flowNode.getOutgoingFlows().forEach(sequenceFlow -> {
            GrooveNode addedToken = addTokenWithPosition(ruleBuilder,
                                                         processInstance,
                                                         getSequenceFlowIdOrDescriptiveName(sequenceFlow, useSFId));
            ruleBuilder.contextEdge(AT, addedToken, quantifier);
        });
    }

    public static GrooveNode contextTokenWithPosition(GrooveRuleBuilder ruleBuilder,
                                                      GrooveNode processInstance,
                                                      String position) {
        GrooveNode token = ruleBuilder.contextNode(TYPE_TOKEN);
        ruleBuilder.contextEdge(TOKENS, processInstance, token);
        ruleBuilder.contextEdge(POSITION, token, ruleBuilder.contextNode(createStringNodeLabel(position)));
        return token;
    }

    public static GrooveNode deleteTokenWithPosition(GrooveRuleBuilder ruleBuilder,
                                                     GrooveNode processInstance,
                                                     String position) {
        GrooveNode token = ruleBuilder.deleteNode(TYPE_TOKEN);
        ruleBuilder.deleteEdge(TOKENS, processInstance, token);
        ruleBuilder.deleteEdge(POSITION, token, ruleBuilder.contextNode(createStringNodeLabel(position)));

        return token;
    }

    public static GrooveNode addTokenWithPosition(GrooveRuleBuilder ruleBuilder,
                                                  GrooveNode processInstance,
                                                  String position) {
        GrooveNode newToken = ruleBuilder.addNode(TYPE_TOKEN);
        ruleBuilder.addEdge(TOKENS, processInstance, newToken);
        ruleBuilder.addEdge(POSITION, newToken, ruleBuilder.contextNode(createStringNodeLabel(position)));
        return newToken;
    }

    public static GrooveNode deleteMessageToProcessInstanceWithPosition(GrooveRuleBuilder ruleBuilder,
                                                                        GrooveNode processInstance,
                                                                        String position) {
        GrooveNode message = ruleBuilder.deleteNode(TYPE_MESSAGE);
        ruleBuilder.deleteEdge(MESSAGES, processInstance, message);
        ruleBuilder.deleteEdge(POSITION, message, ruleBuilder.contextNode(createStringNodeLabel(position)));
        return message;
    }

    static void addExistentialMessageWithPosition(GrooveRuleBuilder ruleBuilder,
                                                  GrooveNode processInstance,
                                                  String position,
                                                  GrooveNode existsOptional) {
        GrooveNode newMessage = ruleBuilder.addNode(TYPE_MESSAGE);
        ruleBuilder.contextEdge(GrooveTransformer.AT, newMessage, existsOptional);
        ruleBuilder.addEdge(MESSAGES, processInstance, newMessage);
        ruleBuilder.addEdge(POSITION, newMessage, ruleBuilder.contextNode(createStringNodeLabel(position)));
    }

    public static void addMessageFlowBehaviorForFlowNode(BPMNCollaboration collaboration,
                                                         GrooveRuleBuilder ruleBuilder,
                                                         FlowNode producingMessageFlowNode,
                                                         boolean useSFId) {
        collaboration.getMessageFlows().stream().filter(messageFlow -> messageFlow.getSource() ==
                                                                       producingMessageFlowNode).forEach(messageFlow -> {
            if (messageFlow.getTarget().isInstantiateFlowNode()) {
                addMessageFlowInstantiateFlowNodeBehavior(ruleBuilder, messageFlow);
            } else if (isAfterInstantiateEventBasedGateway(messageFlow.getTarget())) {
                addMessageFlowInstantiateAfterEVGatewayBehavior(collaboration, ruleBuilder, messageFlow, useSFId);
            } else {
                addMessageSendBehaviorIfProcessExists(collaboration, ruleBuilder, messageFlow, useSFId);
            }
        });
    }

    private static void addMessageSendBehaviorIfProcessExists(BPMNCollaboration collaboration,
                                                              GrooveRuleBuilder ruleBuilder,
                                                              MessageFlow messageFlow,
                                                              boolean useSFId) {
        Process messageFlowReceiver = collaboration.getMessageFlowReceiver(messageFlow);
        // If a process instance exists, send a message.
        GrooveNode existsOptional = ruleBuilder.contextNode(EXISTS_OPTIONAL);
        GrooveNode receiverInstance = contextProcessInstanceWithQuantifier(messageFlowReceiver,
                                                                           ruleBuilder,
                                                                           existsOptional);
        addExistentialMessageWithPosition(ruleBuilder, receiverInstance, messageFlow.getName(), existsOptional);
        // We assume a message receiver can only have one incoming sequence flow if any.
        messageFlow.getTarget().getIncomingFlows().forEach(sequenceFlow -> {
            GrooveNode token = ruleBuilder.contextNode(TYPE_TOKEN);
            ruleBuilder.contextEdge(TOKENS, receiverInstance, token);
            String tokenPosition;
            if (sequenceFlow.getSource().isExclusiveEventBasedGateway()) {
                tokenPosition = sequenceFlow.getSource().getName();
            } else {
                tokenPosition = getSequenceFlowIdOrDescriptiveName(sequenceFlow, useSFId);
            }
            ruleBuilder.contextEdge(POSITION, token, ruleBuilder.contextNode(createStringNodeLabel(tokenPosition)));
            ruleBuilder.contextEdge(AT, token, existsOptional);
        });
        // TODO: Afterwards remove deleting messages from terminate rule.
    }

    private static void addMessageFlowInstantiateFlowNodeBehavior(GrooveRuleBuilder ruleBuilder,
                                                                  MessageFlow messageFlow) {
        // In case of instantiate receive tasks or start events with trigger and active process
        // instance does not exist!
        GrooveNode newMessage = ruleBuilder.addNode(TYPE_MESSAGE);
        ruleBuilder.addEdge(POSITION,
                            newMessage,
                            ruleBuilder.contextNode(createStringNodeLabel(messageFlow.getName())));
    }

    private static void addMessageFlowInstantiateAfterEVGatewayBehavior(BPMNCollaboration collaboration,
                                                                        GrooveRuleBuilder ruleBuilder,
                                                                        MessageFlow messageFlow,
                                                                        boolean useSFId) {
        // TODO: Maybe implement addMessageFlowInstantiateFlowNodeBehavior similarly without sending a message into
        //  nirvana? But then we get the event subprocess interrupt/non-interrupt logic here.
        // Catch rules are not created in this case.
        AbstractProcess newProcess = collaboration.findProcessForFlowNode(messageFlow.getTarget());
        GrooveNode newProcessInstance = addProcessInstance(ruleBuilder, newProcess.getName());
        addOutgoingTokensForFlowNodeToProcessInstance(messageFlow.getTarget(), ruleBuilder, newProcessInstance, useSFId);
    }

    public static boolean isAfterInstantiateEventBasedGateway(FlowNode target) {
        boolean isAfterInstantiateEVGateway =
                target.getIncomingFlows().anyMatch(sequenceFlow -> sequenceFlow.getSource().isExclusiveEventBasedGateway() &&
                                                                                                 sequenceFlow.getSource().isInstantiateFlowNode());
        if (isAfterInstantiateEVGateway && target.getIncomingFlows().count() > 1) {
            throw new BPMNRuntimeException(
                    "Multiple incoming sequence flows into a message event after an instantiate event based gateway! " +
                    "Only the sequence flow from the event based gateway is allowed.");
        }
        return isAfterInstantiateEVGateway;
    }

    public static GrooveNode deleteIncomingMessageAndCreateProcessInstance(MessageFlow incomingMessageFlow,
                                                                           BPMNCollaboration collaboration,
                                                                           GrooveRuleBuilder ruleBuilder) {
        Process receiverProcess = collaboration.getMessageFlowReceiver(incomingMessageFlow);
        GrooveNode message = ruleBuilder.deleteNode(TYPE_MESSAGE);
        ruleBuilder.deleteEdge(POSITION,
                               message,
                               ruleBuilder.contextNode(createStringNodeLabel(incomingMessageFlow.getName())));

        return addProcessInstance(ruleBuilder, receiverProcess.getName());
    }

    public static GrooveNode addTokensForOutgoingFlowsToRunningInstance(FlowNode flowNode,
                                                                        AbstractProcess process,
                                                                        GrooveRuleBuilder ruleBuilder,
                                                                        boolean useSFId) {
        GrooveNode processInstance = contextProcessInstance(process, ruleBuilder);
        addOutgoingTokensForFlowNodeToProcessInstance(flowNode, ruleBuilder, processInstance, useSFId);
        return processInstance;
    }

    public static GrooveNode addTokensForOutgoingFlowsToRunningInstanceWithQuantifier(FlowNode flowNode,
                                                                                      AbstractProcess process,
                                                                                      GrooveRuleBuilder ruleBuilder,
                                                                                      GrooveNode quantifier,
                                                                                      boolean useSFId) {
        GrooveNode processInstance = contextProcessInstanceWithQuantifier(process, ruleBuilder, quantifier);
        addOutgoingTokensForFlowNodeToProcessInstanceWithQuantifier(flowNode,
                                                                    ruleBuilder,
                                                                    processInstance,
                                                                    quantifier,
                                                                    useSFId);
        return processInstance;
    }

    public static GrooveNode deleteAllTokensForProcess(GrooveRuleBuilder ruleBuilder,
                                                       GrooveNode parentProcessInstance) {
        GrooveNode forAll = ruleBuilder.contextNode(FORALL);
        GrooveNode parentToken = ruleBuilder.deleteNode(TYPE_TOKEN);
        ruleBuilder.deleteEdge(TOKENS, parentProcessInstance, parentToken);
        ruleBuilder.contextEdge(AT, parentToken, forAll);
        return forAll;
    }


    public static void interruptSubprocess(GrooveRuleBuilder ruleBuilder,
                                           CallActivity callActivity,
                                           GrooveNode processInstance,
                                           GrooveNode quantifierIfExists) {
        // Terminate subprocess and delete all its tokens.
        GrooveNode subprocessInstance = ruleBuilder.deleteNode(TYPE_PROCESS_SNAPSHOT);
        ruleBuilder.deleteEdge(SUBPROCESS, processInstance, subprocessInstance);
        String subprocessName = callActivity.getSubProcessModel().getName();
        ruleBuilder.deleteEdge(NAME,
                               subprocessInstance,
                               ruleBuilder.contextNode(createStringNodeLabel(subprocessName)));
        GrooveNode subprocessRunning = ruleBuilder.deleteNode(TYPE_RUNNING);
        ruleBuilder.deleteEdge(STATE, subprocessInstance, subprocessRunning);

        // Delete all tokens
        GrooveNode forAllTokens = ruleBuilder.contextNode(FORALL);
        GrooveNode token = ruleBuilder.deleteNode(TYPE_TOKEN);
        ruleBuilder.deleteEdge(TOKENS, subprocessInstance, token);
        ruleBuilder.contextEdge(AT, token, forAllTokens);
        // Delete all messages
        GrooveNode forAllMessages = (quantifierIfExists !=
                                     null) ? ruleBuilder.contextNode(FORALL_NON_VACUOUS) : ruleBuilder.contextNode(
                FORALL); // TODO: Add some explanation. Probably not the final say yet.
        GrooveNode message = ruleBuilder.deleteNode(TYPE_MESSAGE);
        ruleBuilder.deleteEdge(MESSAGES, subprocessInstance, message);
        ruleBuilder.contextEdge(AT, message, forAllMessages);


        if (quantifierIfExists != null) {
            ruleBuilder.contextEdge(AT, subprocessInstance, quantifierIfExists);
            ruleBuilder.contextEdge(AT, subprocessRunning, quantifierIfExists);

            ruleBuilder.contextEdge(IN, forAllTokens, quantifierIfExists);
            ruleBuilder.contextEdge(IN, forAllMessages, quantifierIfExists);
        }
    }

    public static String getStartEventTokenName(Process process, StartEvent event) {
        return process.getName() + "_" + event.getName();
    }

    public static String getSequenceFlowIdOrDescriptiveName(SequenceFlow flow, boolean useID) {
        return useID ? flow.getId() : flow.getDescriptiveName();
    }
}
