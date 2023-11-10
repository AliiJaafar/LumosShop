package com.lumosshop.common.entity.control;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "control-center")
public class Control {
    @Id
    @Column(name = "`key`", nullable = false, length = 128)
    private String key;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;
    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private ControlType type;

    public Control(String key) {
        this.key = key;
    }

    public Control(String key, String value, ControlType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public Control() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ControlType getType() {
        return type;
    }

    public void setType(ControlType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Control that = (Control) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Control{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
