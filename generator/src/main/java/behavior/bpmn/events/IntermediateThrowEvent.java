package behavior.bpmn.events;

import behavior.bpmn.auxiliary.EventVisitor;
import behavior.bpmn.auxiliary.FlowNodeVisitor;
import com.google.common.base.Objects;

public class IntermediateThrowEvent extends ThrowEvent {
    private final IntermediateThrowEventType type;

    public IntermediateThrowEvent(String id, String name, IntermediateThrowEventType type) {
        this(id, name, type, EventDefinition.empty());
    }

    public IntermediateThrowEvent(String id,
                                  String name,
                                  IntermediateThrowEventType type,
                                  EventDefinition eventDefinition) {
        super(id, name, eventDefinition);
        this.type = type;
    }

    @Override
    public void accept(FlowNodeVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public boolean isInstantiateFlowNode() {
        return false;
    }

    public IntermediateThrowEventType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        IntermediateThrowEvent that = (IntermediateThrowEvent) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), type);
    }
}
