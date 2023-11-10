package com.lumosshop.common.entity.control;

import java.util.List;

import java.util.List;

public class ControlCenter {
    private List<Control> controlList;

    public ControlCenter(List<Control> controlList) {
        this.controlList = controlList;
    }

    public Control get(String key) {
        int index = controlList.indexOf(new Control(key));

        if (index >= 0) {
            return controlList.get(index);
        }

        return null;
    }

    public String getValue(String key) {
        Control control = get(key);

        if (control != null) {
            return control.getValue();
        }
        return null;
    }
    public void update(String key, String value) {
        Control control = get(key);
        if (control != null && value != null) {
            control.setValue(value);
        }
    }

    public List<Control> list() {
        return controlList;
    }

}
