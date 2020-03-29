package org.mvnsearch.boot.npm.export.generator;

import org.intellij.lang.annotations.Language;

import java.util.HashMap;
import java.util.Map;

/**
 * package.json generator
 *
 * @author linux_china
 */
public class PackageJsonGenerator {
    @Language("JSON")
    private String templateText = "{\n" +
            "  \"name\": \"$packageName\",\n" +
            "  \"version\": \"1.0.0\",\n" +
            "  \"main\": \"index.js\",\n" +
            "  \"dependencies\": {\n" +
            "    \"axios\": \"^0.19.2\"\n" +
            "  }\n" +
            "}\n";
    private Map<String, String> context = new HashMap<>();

    public PackageJsonGenerator() {

    }

    public PackageJsonGenerator addContext(String key, String value) {
        this.context.put(key, value);
        return this;
    }

    public String generate() {
        String result = templateText;
        for (Map.Entry<String, String> entry : context.entrySet()) {
            result = result.replace("$" + entry.getKey(), entry.getValue());
        }
        return result;
    }
}
