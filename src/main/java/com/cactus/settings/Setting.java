package com.cactus.settings;

public abstract class Setting<T> {

    private final String name;
    private T value;
    private final String id;

    public Setting(String name, String id, T defaultValue) {
        this.name = name;
        this.id = id;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}