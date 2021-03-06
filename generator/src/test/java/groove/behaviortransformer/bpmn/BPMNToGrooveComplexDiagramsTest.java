package groove.behaviortransformer.bpmn;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class BPMNToGrooveComplexDiagramsTest extends BPMNToGrooveTestBase {

    /**
     * See test case <a href="https://cawemo.com/share/9b143426-50ed-4621-83af-b30e29273077">"Cyclic"</a> in cawemo.
     */
    @Test
    void testCyclic() throws IOException {
        testGrooveGenerationForBPMNResourceFile("cyclic.bpmn");
    }
}
