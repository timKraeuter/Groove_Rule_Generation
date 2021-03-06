package groove.behaviortransformer.bpmn;

import behavior.bpmn.BPMNCollaboration;
import behavior.bpmn.Process;
import behavior.bpmn.auxiliary.exceptions.ShouldNotHappenRuntimeException;
import behavior.bpmn.events.StartEventType;
import groove.behaviortransformer.GrooveTransformer;
import groove.graph.GrooveGraph;
import groove.graph.GrooveGraphBuilder;
import groove.graph.GrooveNode;
import groove.graph.rule.GrooveGraphRule;
import groove.graph.rule.GrooveRuleBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static groove.behaviortransformer.GrooveTransformerHelper.createStringNodeLabel;
import static groove.behaviortransformer.bpmn.BPMNToGrooveTransformerConstants.*;
import static groove.behaviortransformer.bpmn.BPMNToGrooveTransformerHelper.getStartEventTokenName;

public class BPMNToGrooveTransformer implements GrooveTransformer<BPMNCollaboration> {

    public static final String TYPE_GRAPH_FILE_NAME = "bpmn_e_model.gty";
    public static final String TERMINATE_RULE_FILE_NAME = "Terminate.gpr";
    // Graph conditions for model-checking
    public static final String ALL_TERMINATED_FILE_NAME = "AllTerminated.gpr";
    public static final String UNSAFE_FILE_NAME = "Unsafe.gpr";
    private boolean useSFIds;

    public BPMNToGrooveTransformer() {
        this(false);
    }

    public BPMNToGrooveTransformer(boolean useSFId) {
        this.useSFIds = useSFId;
    }

    @Override
    public GrooveGraph generateStartGraph(BPMNCollaboration collaboration) {
        GrooveGraphBuilder startGraphBuilder = new GrooveGraphBuilder().setName(collaboration.getName());

        collaboration.getParticipants().stream().filter(process -> process.getStartEvents().stream()
                                                                          .anyMatch(startEvent -> startEvent.getType() == StartEventType.NONE))
                                                .forEach(process -> {
            GrooveNode processInstance = new GrooveNode(TYPE_PROCESS_SNAPSHOT);
            GrooveNode processName = new GrooveNode(createStringNodeLabel(process.getName()));
            startGraphBuilder.addEdge(NAME, processInstance, processName);
            GrooveNode running = new GrooveNode(TYPE_RUNNING);
            startGraphBuilder.addEdge(STATE, processInstance, running);
            addStartTokens(startGraphBuilder, process, processInstance);
        });

        return startGraphBuilder.build();
    }

    private void addStartTokens(GrooveGraphBuilder startGraphBuilder, Process process, GrooveNode processInstance) {
        process.getStartEvents().forEach(startEvent -> {
            if (startEvent.getType() == StartEventType.NONE) {
                GrooveNode startToken = new GrooveNode(TYPE_TOKEN);
                GrooveNode tokenName = new GrooveNode(createStringNodeLabel(getStartEventTokenName(process,
                                                                                                   startEvent)));
                startGraphBuilder.addEdge(POSITION, startToken, tokenName);
                startGraphBuilder.addEdge(TOKENS, processInstance, startToken);
            }
        });
    }

    @Override
    public Stream<GrooveGraphRule> generateRules(BPMNCollaboration collaboration) {
        GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
        BPMNRuleGenerator bpmnRuleGenerator = new BPMNRuleGenerator(ruleBuilder, collaboration, useSFIds);

        return bpmnRuleGenerator.getRules();
    }

    @Override
    public void generateAndWriteRulesFurther(BPMNCollaboration collaboration, File targetFolder) {
        this.copyTypeGraphAndFixedRules(targetFolder);
    }

    private void copyTypeGraphAndFixedRules(File targetFolder) {
        InputStream typeGraph = this.getClass().getResourceAsStream(FIXED_RULES_AND_TYPE_GRAPH_DIR +
                                                                    TYPE_GRAPH_FILE_NAME);
        InputStream terminateRule = this.getClass().getResourceAsStream(FIXED_RULES_AND_TYPE_GRAPH_DIR +
                                                                        TERMINATE_RULE_FILE_NAME);
        InputStream unsafeGraph = this.getClass().getResourceAsStream(FIXED_RULES_AND_TYPE_GRAPH_DIR +
                                                                      UNSAFE_FILE_NAME);
        InputStream allterminatedGraph = this.getClass().getResourceAsStream(FIXED_RULES_AND_TYPE_GRAPH_DIR +
                                                                             ALL_TERMINATED_FILE_NAME);
        try {
            FileUtils.copyInputStreamToFile(typeGraph, new File(targetFolder, TYPE_GRAPH_FILE_NAME));
            FileUtils.copyInputStreamToFile(terminateRule, new File(targetFolder, TERMINATE_RULE_FILE_NAME));
            FileUtils.copyInputStreamToFile(unsafeGraph, new File(targetFolder, UNSAFE_FILE_NAME));
            FileUtils.copyInputStreamToFile(allterminatedGraph, new File(targetFolder, ALL_TERMINATED_FILE_NAME));
        }
        catch (IOException e) {
            throw new ShouldNotHappenRuntimeException(e);
        }
    }

    @Override
    public boolean isLayoutActivated() {
        return true; // TODO: implement layout as parameter!
    }
}
