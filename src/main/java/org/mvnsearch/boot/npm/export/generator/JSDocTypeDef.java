package org.mvnsearch.boot.npm.export.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * JSDoc TypeDef
 *
 * @author linux_china
 */
public class JSDocTypeDef {
    private String name;
    private List<String> properties = new ArrayList<>();

    public JSDocTypeDef(String name) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public void addProperty(String property) {
        this.properties.add(property);
    }

    public void addProperty(String name, String type) {
        this.properties.add("{" + type + "} " + name);
    }
}
