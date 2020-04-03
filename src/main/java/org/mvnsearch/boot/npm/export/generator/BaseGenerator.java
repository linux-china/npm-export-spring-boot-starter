package org.mvnsearch.boot.npm.export.generator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base Generator
 *
 * @author linux_china
 */
public class BaseGenerator implements JavaToJsTypeConverter {
    protected final Class<?> controllerClass;
    protected final String jsClassName;
    protected final List<Method> requestMethods;
    protected final List<JsHttpStubMethod> jsHttpStubMethods;
    /**
     * javabean for typeDef from @Schema implementation
     */
    protected final Map<Class<?>, String> javaBeanTypeDefMap = new HashMap<>();
    /**
     * customized typedef from @Schema properties
     */
    protected Map<String, JSDocTypeDef> customizedTypeDefMap = new HashMap<>();
    protected String basePath;

    public BaseGenerator(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
        if (requestMapping != null) {
            String[] basePaths = requestMapping.value();
            if (basePaths.length > 0) {
                this.basePath = basePaths[0];
            }
        }
        this.requestMethods = Arrays.stream(this.controllerClass.getMethods())
                .filter(method -> AnnotationUtils.findAnnotation(method, RequestMapping.class) != null)
                .collect(Collectors.toList());
        this.jsHttpStubMethods = this.requestMethods.stream()
                .map(this::generateMethodStub)
                .collect(Collectors.toList());
        this.jsClassName = controllerClass.getSimpleName();
    }


    public JsHttpStubMethod generateMethodStub(Method method) {
        JsHttpStubMethod stubMethod = new JsHttpStubMethod();
        stubMethod.setName(method.getName());
        //@deprecated
        Deprecated deprecated = method.getAnnotation(Deprecated.class);
        if (deprecated != null) {
            stubMethod.setDeprecated(true);
        }
        //@Operation from OpenAPI
        Operation operation = method.getAnnotation(Operation.class);
        if (operation != null) {
            stubMethod.setDescription(operation.description());
        }
        //@Nullable
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.getClass().getSimpleName().equals("Nullable")) {
                stubMethod.setResultNullable(true);
            }
        }
        //@RequestMapping
        String[] paths = null;
        RequestMethod requestMethod = null;
        RequestMapping requestMapping = findAnnotationWithAttributesMerged(method, RequestMapping.class);
        if (requestMapping != null) {
            paths = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            if (requestMethods.length > 0) {
                requestMethod = requestMethods[0];
            }
        }
        stubMethod.setMethod(requestMethod == null ? RequestMethod.POST : requestMethod);
        if (paths != null && paths.length > 0) {
            stubMethod.setPath(paths[0]);
        }
        if (basePath != null && !basePath.isEmpty()) {
            stubMethod.setPath(basePath + stubMethod.getPath());
        }
        //parameters
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            for (Parameter parameter : parameters) {
                JsParam jsParam = new JsParam();
                jsParam.setName(parameter.getName());
                jsParam.setType(parameter.getType());
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                if (pathVariable != null) {
                    String value = pathVariable.value();
                    if (value.isEmpty()) {
                        value = jsParam.getName();
                    }
                    jsParam.setPathVariableName(value);
                    jsParam.setRequired(pathVariable.required());
                }
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                if (requestParam != null) {
                    String value = requestParam.value();
                    if (value.isEmpty()) {
                        value = parameter.getName();
                    }
                    jsParam.setDefaultValue(requestParam.defaultValue());
                    jsParam.setRequestParamName(value);
                    jsParam.setRequired(requestParam.required());
                }
                RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
                if (requestHeader != null) {
                    String value = requestHeader.value();
                    jsParam.setHttpHeaderName(value);
                    jsParam.setDefaultValue(requestHeader.defaultValue());
                    jsParam.setRequired(requestHeader.required());
                }
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                if (requestBody != null) {
                    jsParam.setBodyData(true);
                    Class<?> bodyType = parameter.getType();
                    if (bodyType.isAssignableFrom(String.class)) {
                        stubMethod.setRequestContentType("text/plain");
                    } else if (bodyType.isAssignableFrom(ByteBuffer.class)
                            || bodyType.isAssignableFrom(byte[].class)) {
                        stubMethod.setRequestContentType("application/octet-stream");
                    } else {
                        stubMethod.setRequestContentType("application/json");
                    }
                    jsParam.setRequired(requestBody.required());
                    //parameter schema
                    Schema schema = findAnnotationWithAttributesMerged(parameter, Schema.class);
                    if (schema != null) {
                        //java class as response
                        if (schema.implementation() != Void.class) {
                            jsParam.setType(schema.implementation());
                        } else if (schema.requiredProperties().length > 0) {
                            JSDocTypeDef jsDocTypeDef = new JSDocTypeDef(schema.name());
                            for (String property : schema.requiredProperties()) {
                                jsDocTypeDef.addProperty(property);
                            }
                            jsParam.setJsDocTypeDef(jsDocTypeDef);
                        }
                    }
                }
                stubMethod.addParam(jsParam);
            }
        }
        //return type
        Type genericReturnType = method.getGenericReturnType();
        stubMethod.setReturnType(parseInferredClass(genericReturnType));
        //@Schema
        Schema schema = findAnnotationWithAttributesMerged(method, Schema.class);
        if (schema != null) {
            //java class as response
            if (schema.implementation() != Void.class) {
                stubMethod.setReturnType(schema.implementation());
            } else if (schema.requiredProperties().length > 0) {
                JSDocTypeDef jsDocTypeDef = new JSDocTypeDef(schema.name());
                for (String property : schema.requiredProperties()) {
                    jsDocTypeDef.addProperty(property);
                }
                stubMethod.setJsDocTypeDef(jsDocTypeDef);
            }
        }
        return stubMethod;
    }

    public static Class<?> parseInferredClass(Type genericType) {
        Class<?> inferredClass = null;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) genericType;
            Type[] typeArguments = type.getActualTypeArguments();
            if (typeArguments.length > 0) {
                final Type typeArgument = typeArguments[0];
                if (typeArgument instanceof ParameterizedType) {
                    inferredClass = (Class<?>) ((ParameterizedType) typeArgument).getActualTypeArguments()[0];
                } else {
                    inferredClass = (Class<?>) typeArgument;
                }
            }
        }
        if (inferredClass == null && genericType instanceof Class) {
            inferredClass = (Class<?>) genericType;
        }
        return inferredClass;
    }

    @Nullable
    public static <A extends Annotation> A findAnnotationWithAttributesMerged(AnnotatedElement element, Class<A> annotationType) {
        A annotation = AnnotationUtils.findAnnotation(element, annotationType);
        if (annotation != null) {
            annotation = MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none())
                    .get(annotationType)
                    .synthesize(MergedAnnotation::isPresent).orElse(null);
        }
        return annotation;
    }
}
