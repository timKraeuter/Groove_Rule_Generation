package groove.behaviortransformer.bpmn;

import behavior.bpmn.AbstractProcess;
import behavior.bpmn.BPMNCollaboration;
import behavior.bpmn.FlowNode;
import behavior.bpmn.Process;
import com.google.common.collect.Sets;
import groove.behaviortransformer.bpmn.generators.*;
import groove.graph.GrooveNode;
import groove.graph.rule.GrooveGraphRule;
import groove.graph.rule.GrooveRuleBuilder;

import java.util.Set;
import java.util.stream.Stream;

import static groove.behaviortransformer.GrooveTransformerHelper.createStringNodeLabel;
import static groove.behaviortransformer.bpmn.BPMNToGrooveTransformerConstants.*;

public class BPMNRuleGenerator {
    private final GrooveRuleBuilder ruleBuilder;
    private final BPMNCollaboration collaboration;
    private final Set<Process> visitedProcessModels;

    // Subgenerators
    private final BPMNTaskRuleGenerator taskRuleGenerator;
    private final BPMNEventRuleGenerator eventRuleGenerator;
    private final BPMNGatewayRuleGenerator gatewayRuleGenerator;
    private final BPMNEventSubprocessRuleGenerator eventSubprocessRuleGenerator;
    private final BPMNSubprocessRuleGenerator subprocessRuleGenerator;

    BPMNRuleGenerator(GrooveRuleBuilder ruleBuilder, BPMNCollaboration collaboration, boolean useSFId) {
        this.ruleBuilder = ruleBuilder;
        this.collaboration = collaboration;
        visitedProcessModels = Sets.newHashSet();

        taskRuleGenerator = new BPMNTaskRuleGeneratorImpl(collaboration, ruleBuilder, useSFId);
        eventRuleGenerator = new BPMNEventRuleGeneratorImpl(this, ruleBuilder, useSFId);
        gatewayRuleGenerator = new BPMNGatewayRuleGeneratorImpl(ruleBuilder, useSFId);
        eventSubprocessRuleGenerator = new BPMNEventSubprocessRuleGeneratorImpl(this, ruleBuilder, useSFId);
        subprocessRuleGenerator = new BPMNSubprocessRuleGeneratorImpl(this, ruleBuilder, useSFId);

        generateRules();
    }

    public Stream<GrooveGraphRule> getRules() {
        return ruleBuilder.getRules();
    }

    public Set<Process> getVisitedProcessModels() {
        return visitedProcessModels;
    }

    public BPMNCollaboration getCollaboration() {
        return collaboration;
    }

    public BPMNEventSubprocessRuleGenerator getEventSubprocessRuleGenerator() {
        return eventSubprocessRuleGenerator;
    }

    public BPMNSubprocessRuleGenerator getSubprocessRuleGenerator() {
        return subprocessRuleGenerator;
    }

    public BPMNGatewayRuleGenerator getGatewayRuleGenerator() {
        return gatewayRuleGenerator;
    }

    public BPMNTaskRuleGenerator getTaskRuleGenerator() {
        return taskRuleGenerator;
    }

    public BPMNEventRuleGenerator getEventRuleGenerator() {
        return eventRuleGenerator;
    }

    private void generateRules() {
        collaboration.getParticipants().forEach(process -> {
            if (!visitedProcessModels.contains(process)) {
                visitedProcessModels.add(process);
                generateRulesForProcess(process);
            }
        });
    }

    public void generateRulesForProcess(AbstractProcess process) {
        process.getFlowNodes().forEach(node -> node.accept(new RuleGenerationFlowNodeVisitor(this, process)));

        getEventSubprocessRuleGenerator().generateRulesForEventSubprocesses(process);
    }

    // Methods shared between the generators.
    public String getTaskOrCallActivityRuleName(FlowNode taskOrCallActivity, String incomingFlowId) {
        if (taskOrCallActivity.getIncomingFlows().count() > 1) {
            return taskOrCallActivity.getName() + "_" + incomingFlowId;
        }
        return taskOrCallActivity.getName();
    }

    public void deleteTerminatedSubprocess(GrooveRuleBuilder ruleBuilder,
                                           String eSubprocessName,
                                           GrooveNode parentProcessInstance) {
        GrooveNode subProcessInstance = ruleBuilder.deleteNode(TYPE_PROCESS_SNAPSHOT);
        ruleBuilder.deleteEdge(NAME,
                               subProcessInstance,
                               ruleBuilder.contextNode(createStringNodeLabel(eSubprocessName)));
        ruleBuilder.deleteEdge(SUBPROCESS, parentProcessInstance, subProcessInstance);
        GrooveNode terminated = ruleBuilder.deleteNode(TYPE_TERMINATED);
        ruleBuilder.deleteEdge(STATE, subProcessInstance, terminated);
    }
}
