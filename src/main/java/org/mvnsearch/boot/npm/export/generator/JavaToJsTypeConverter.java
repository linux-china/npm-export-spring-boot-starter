package org.mvnsearch.boot.npm.export.generator;

import java.lang.reflect.Field;
import java.util.*;

/**
 * java to javascript type converter
 *
 * @author linux_china
 */
public interface JavaToJsTypeConverter {
    List<Class<?>> numberClazzList = Arrays.asList(Integer.class, int.class,
            Long.class, long.class,
            Float.class, float.class,
            Double.class, double.class,
            Byte.class, byte.class);
    List<String> jsTypes = Arrays.asList("string", "boolean", "number", "Array", "Object");

    default String toJsType(Class<?> type) {
        if (type.isAssignableFrom(String.class)
                || type.isAssignableFrom(Date.class)
                || type.getCanonicalName().startsWith("java.time.")
                || type.isAssignableFrom(UUID.class)) {
            return "string";
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
            return "boolean";
        } else if (numberClazzList.contains(type)) {
            return "number";
        } else if (type.isAssignableFrom(List.class)
                || type.isAssignableFrom(Set.class)
                || type.isArray()) {
            return "Array";
        } else if (type.isAssignableFrom(Map.class)) {
            return "Object";
        } else {
            return type.getCanonicalName().replaceAll("\\.", "_");
        }
    }

    default String toTypeDef(Class<?> clazz) {
        StringBuilder builder = new StringBuilder();
        builder.append("/**\n");
        builder.append(" * @typedef {Object} " + clazz.getSimpleName() + "\n");
        for (Field field : clazz.getDeclaredFields()) {
            builder.append(" * @property {" + toJsType(field.getType()) + "} " + field.getName() + "\n");
        }
        builder.append(" */\n");
        return builder.toString();
    }
}
