package org.mvnsearch.boot.npm.export.generator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TypeScript Declaration generator: index.d.ts
 *
 * @author linux_china
 */
public class TypeScriptDeclarationGenerator extends BaseGenerator {

    public TypeScriptDeclarationGenerator(Class<?> controllerClass) {
        super(controllerClass);
    }

    public String generate() {
        String global = "/**\n" +
                " * set base url, such as http://localhost:8080\n" +
                " * @param url http url\n" +
                " */\n" +
                "export function setBaseUrl(url: string): void;\n" +
                "\n" +
                "/**\n" +
                " * set JWT token\n" +
                " * @param token  JWT token\n" +
                " */\n" +
                "export function setJwtToken(token: string): void;\n" +
                "\n" +
                "/**\n" +
                " * set axios config object filter\n" +
                " * @param filter  axios config object\n" +
                " */\n" +
                "export function setConfigFilter(filter: (config: Object) => Object): void;\n\n";
        StringBuilder builder = new StringBuilder();
        builder.append(global);
        for (JsHttpStubMethod stubMethod : jsHttpStubMethods) {
            builder.append(toTypeScriptDeclarationMethod(stubMethod) + "\n\n");
        }
        builder.append(typeScriptClasses());
        return builder.toString();
    }

    public String toTypeScriptDeclarationMethod(JsHttpStubMethod stubMethod) {
        StringBuilder builder = new StringBuilder();
        builder.append("export function " + stubMethod.getName() + "(");
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
            builder.append("declare class " + entry.getValue() + " {\n");
            for (Field field : clazz.getDeclaredFields()) {
                builder.append("  " + field.getName() + ": " + toTsType(toJsType(field.getType()) + "\n"));
            }
            builder.append("}\n\n");
        }
        //@typeDef for return type and parameter type
        Map<String, JSDocTypeDef> allTypeDefMap = new HashMap<>(this.customizedTypeDefMap);
        Map<String, JSDocTypeDef> typeDefForReturnTypeMap = jsHttpStubMethods.stream()
                .map(JsHttpStubMethod::getJsDocTypeDef)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(JSDocTypeDef::getName, jsDocTypeDef -> jsDocTypeDef, (a, b) -> b));
        allTypeDefMap.putAll(typeDefForReturnTypeMap);
        for (JSDocTypeDef jsDocTypeDef : allTypeDefMap.values()) {
            builder.append("declare class " + jsDocTypeDef.getName() + " {\n");
            for (String property : jsDocTypeDef.getProperties()) {
                builder.append("  " + property + "\n");
            }
            builder.append("}\n\n");
        }
        return builder.toString();
    }
}
