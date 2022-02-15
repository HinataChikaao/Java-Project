package util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StatusContainer implements Serializable{

    public enum StatusType {
        Disable, Enable
    }

    private Map<String, Boolean> status;

    public StatusContainer(Class<?> components, StatusType default_status) {
        if (components.isEnum()) {
            status = new HashMap<>();
            for (Field field : components.getFields()) {
                status.put(field.getName(), default_status == StatusType.Disable);
            }
        }
    }

    public StatusContainer(Class<?> components) {
        if (components.isEnum()) {
            status = new HashMap<>();
            for (Field field : components.getFields()) {
                status.put(field.getName(), false);
            }
        }
    }

    public void setStatus(Object componentName, StatusType value) {
        status.put(String.valueOf(componentName), value == StatusType.Disable);
    }

    public void enable(Object componentName) {
        status.put(String.valueOf(componentName), false);
    }

    public void disable(Object componentName) {
        status.put(String.valueOf(componentName), true);
    }


    public void enableExcept(Object... componentNames) {
        for (Map.Entry<String, Boolean> map : status.entrySet()) {
            map.setValue(false);
        }

        for (Object field : componentNames) {
            status.put(String.valueOf(field), true);
        }
    }

    public void disableExept(Object... componentNames) {
        for (Map.Entry<String, Boolean> map : status.entrySet()) {
            map.setValue(true);
        }
        for (Object field : componentNames) {
            status.put(String.valueOf(field), false);
        }
    }


    public void enableAll() {
        for (Map.Entry<String, Boolean> map : status.entrySet()) {
            map.setValue(false);
        }
    }


    public void disableAll() {
        for (Map.Entry<String, Boolean> map : status.entrySet()) {
            map.setValue(true);
        }
    }

    public boolean getStatus(Object componentName) {
        return status.get(String.valueOf(componentName));
    }

}
