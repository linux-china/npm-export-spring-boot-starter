package org.mvnsearch.boot.npm.export.generator;

import org.intellij.lang.annotations.Language;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Deno's mod.ts generator
 *
 * @author linux_china
 */
public class DenoModGenerator extends BaseGenerator {
    private String baseUrl;

    public DenoModGenerator(Class<?> controllerClass, String baseUrl) {
        super(controllerClass);
        this.baseUrl = baseUrl;
    }

    public String generate() {
        @Language(value = "TypeScript", suffix = "}")
        String global =
                "export class XxxxController {\n" +
                        "    private baseUrl: string;\n" +
                        "    private token?: string | undefined;\n" +
                        "\n" +
                        "    constructor(baseUrl: string) {\n" +
                        "        this.baseUrl = baseUrl;\n" +
                        "    }\n" +
                        "\n" +
                        "    setBaseUrl(baseUrl: string) {\n" +
                        "        this.baseUrl = baseUrl;\n" +
                        "    }\n" +
                        "\n" +
                        "    setJwtToken(token: string) {\n" +
                        "        this.token = token;\n" +
                        "    }\n" +
                        "\n" +
                        "    getDefaultHeaders(): any {\n" +
                        "        let headers: any = {};\n" +
                        "        if (this.token) {\n" +
                        "            headers[\"Authorization\"] = 'Bearer ' + this.token\n" +
                        "        }\n" +
                        "        return headers;\n" +
                        "    }\n";
        StringBuilder builder = new StringBuilder();
        builder.append(global.replaceAll("XxxxController", jsClassName));
        for (JsHttpStubMethod stubMethod : jsHttpStubMethods) {
            builder.append(toTypeScriptDeclarationMethod(stubMethod) + "\n\n");
        }
        builder.append("}\n\n");
        builder.append(typeScriptClasses());
        String variableName = jsClassName.substring(0, 1).toLowerCase() + jsClassName.substring(1);
        builder.append("export const " + variableName + " = new " + jsClassName + " ('" + baseUrl + "');\n ");
        return builder.toString();
    }

    public String toTypeScriptDeclarationMethod(JsHttpStubMethod stubMethod) {
        StringBuilder builder = new StringBuilder();
        builder.append("    " + stubMethod.getName() + "(");
        if (!stubMethod.getParams().isEmpty()) {
            String paramsDeclare = stubMethod.getParams().stream()
                    .filter(JsParam::isFromRequestSide)
                    .map(param -> {
                        JSDocTypeDef jsDocTypeDef = param.getJsDocTypeDef();
                        if (jsDocTypeDef != null) {
                            this.customizedTypeDefMap.put(jsDocTypeDef.getName(), jsDocTypeDef);
                        }
                        return param.getName() + ": " + toTsType(param.getJsType());
                    })
                    .collect(Collectors.joining(", "));
            builder.append(paramsDeclare);
        }
        //java bean type
        String jsReturnType = stubMethod.getJsReturnType();
        if (stubMethod.getJsDocTypeDef() == null && jsReturnType.contains("_")) {
            this.javaBeanTypeDefMap.put(stubMethod.getReturnType(), jsReturnType);
        }
        builder.append("): Promise<" + toTsType(stubMethod.getJsReturnType()) + ">;");
        return builder.toString();
    }

    public String typeScriptClasses() {
        StringBuilder builder = new StringBuilder();
        builder.append("//================ TypeScript Class ========================//\n");
        for (Map.Entry<Class<?>, String> entry : javaBeanTypeDefMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            builder.append("class " + entry.getValue() + " {\n");
            for (Field field : clazz.getDeclaredFields()) {
                builder.append("  " + field.getName() + ": " + toTsType(toJsType(field.getType()) + "\n"));
            }
            builder.append("}\n\n");
        }
        return builder.toString();
    }
}
