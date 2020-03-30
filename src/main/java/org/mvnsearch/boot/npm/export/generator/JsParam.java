package org.mvnsearch.boot.npm.export.generator;

/**
 * js method param
 *
 * @author linux_china
 */
public class JsParam implements JavaToJsTypeConverter {
    private String name;
    private Class<?> type;
    private JSDocTypeDef jsDocTypeDef;
    private String defaultValue;
    private boolean required;
    private String pathVariableName;
    private String requestParamName;
    private String httpHeaderName;
    private boolean bodyData;

    public JsParam() {
    }

    public JsParam(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public JSDocTypeDef getJsDocTypeDef() {
        return jsDocTypeDef;
    }

    public void setJsDocTypeDef(JSDocTypeDef jsDocTypeDef) {
        this.jsDocTypeDef = jsDocTypeDef;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getJsType() {
        if (this.jsDocTypeDef != null) {
            return this.jsDocTypeDef.getName();
        } else {
            return toJsType(this.type);
        }
    }

    public String getPathVariableName() {
        return pathVariableName;
    }

    public void setPathVariableName(String pathVariableName) {
        this.pathVariableName = pathVariableName;
    }

    public String getRequestParamName() {
        return requestParamName;
    }

    public void setRequestParamName(String requestParamName) {
        this.requestParamName = requestParamName;
    }

    public String getHttpHeaderName() {
        return httpHeaderName;
    }

    public void setHttpHeaderName(String httpHeaderName) {
        this.httpHeaderName = httpHeaderName;
    }

    public boolean isBodyData() {
        return bodyData;
    }

    public void setBodyData(boolean bodyData) {
        this.bodyData = bodyData;
    }

    public boolean isFromRequestSide() {
        return this.pathVariableName != null
                || this.requestParamName != null
                || this.httpHeaderName != null
                || this.bodyData;
    }
}
