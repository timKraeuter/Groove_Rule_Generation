package util;

public class ValueWrapper<T> {
    private T value;

    public ValueWrapper() {
    }

    /**
     * @return the wrapped value which might be null.
     */
    public T getValueIfExists() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
