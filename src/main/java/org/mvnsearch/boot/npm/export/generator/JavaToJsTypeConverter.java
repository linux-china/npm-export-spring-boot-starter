package org.mvnsearch.boot.npm.export.generator;

/**
 * java to javascript type converter
 *
 * @author linux_china
 */
public interface JavaToJsTypeConverter {
    default String toJsType(Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            return "string";
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
            return "bool";
        } else if (type.isAssignableFrom(Integer.class)
                || type.isAssignableFrom(int.class)) {
            return "number";
        } else {
            return "Object";
        }
    }
}
