package com.lumosshop.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemSet {
    private List<String> items;

    public ItemSet() {
        this.items = new ArrayList<>();
    }
    public ItemSet(List<String> items) {
        this.items = new ArrayList<>(items);
    }

    public List<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }
    public void setItems(List<String> items) {
        this.items = items;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemSet itemSet = (ItemSet) o;

        return Objects.equals(items, itemSet.items);
    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{" + String.join(", ", items) + "}";
    }

}
