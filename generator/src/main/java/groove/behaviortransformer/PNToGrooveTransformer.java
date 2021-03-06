package groove.behaviortransformer;

import behavior.petrinet.PetriNet;
import behavior.petrinet.Place;
import groove.graph.GrooveEdge;
import groove.graph.GrooveGraph;
import groove.graph.GrooveNode;
import groove.graph.rule.GrooveGraphRule;
import groove.graph.rule.GrooveRuleBuilder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

public class PNToGrooveTransformer implements GrooveTransformer<PetriNet> {

    private static final String TOKEN_NODE_NAME = "Token";
    private static final String TOKEN_EDGE_NAME = "token";

    @Override
    public GrooveGraph generateStartGraph(PetriNet petriNet) {
        Set<GrooveNode> nodes = new LinkedHashSet<>();
        Set<GrooveEdge> edges = new LinkedHashSet<>();

        // Create a node for each place of the petri net.
        petriNet.getPlaces().forEach(place -> {
            GrooveNode placeNode = new GrooveNode(place.getName());
            nodes.add(placeNode);
            // Create and link start tokens for each place.
            for (int i = 0; i < place.getStartTokenAmount(); i++) {
                GrooveNode tokenNode = new GrooveNode(TOKEN_NODE_NAME);
                nodes.add(tokenNode);

                GrooveEdge tokenEdge = new GrooveEdge(TOKEN_EDGE_NAME, placeNode, tokenNode);
                edges.add(tokenEdge);
            }
        });
        return new GrooveGraph(petriNet.getName(), nodes, edges);
    }

    @Override
    public Stream<GrooveGraphRule> generateRules(PetriNet petriNet) {
        GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
        petriNet.getTransitions().forEach(transition -> {
            ruleBuilder.startRule(transition.getName());

            transition.getIncomingEdges().forEach(weigthPlacePair -> {
                Place place = weigthPlacePair.getRight();
                Integer weight = weigthPlacePair.getLeft();
                GrooveNode placeNode = ruleBuilder.contextNode(place.getName());
                for (int i = 0; i < weight; i++) {
                    GrooveNode toBeDeletedTokenNode = ruleBuilder.deleteNode(TOKEN_NODE_NAME);
                    ruleBuilder.deleteEdge(TOKEN_EDGE_NAME, placeNode, toBeDeletedTokenNode);
                }
            });

            transition.getOutgoingEdges().forEach(weigthPlacePair -> {
                Place place = weigthPlacePair.getRight();
                Integer weight = weigthPlacePair.getLeft();
                GrooveNode placeNode = ruleBuilder.contextNode(place.getName());
                for (int i = 0; i < weight; i++) {
                    GrooveNode toBeAddedTokenNode = ruleBuilder.addNode(TOKEN_NODE_NAME);
                    ruleBuilder.addEdge(TOKEN_EDGE_NAME, placeNode, toBeAddedTokenNode);
                }
            });

            ruleBuilder.buildRule();
        });
        return ruleBuilder.getRules();
    }

    @Override
    public boolean isLayoutActivated() {
        return true; // TODO: implement layout as parameter!
    }
}
