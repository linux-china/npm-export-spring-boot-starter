package org.mvnsearch.boot.npm.export.generator;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * JS http stub method
 *
 * @author linux_china
 */
public class JsHttpStubMethod implements JavaToJsTypeConverter {
    private String name;
    private String path;
    private RequestMethod method;
    private List<JsParam> params = new ArrayList<>();
    private Class<?> returnType;
    private Class<?> bodyClass;
    private String requestContentType;
    private boolean deprecated = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public List<JsParam> getParams() {
        return params;
    }

    public void setParams(List<JsParam> params) {
        this.params = params;
    }

    public void addParam(JsParam param) {
        params.add(param);
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getJsReturnType() {
        return toJsType(this.returnType);
    }

    public Class<?> getBodyClass() {
        return bodyClass;
    }

    public void setBodyClass(Class<?> bodyClass) {
        this.bodyClass = bodyClass;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public boolean hasPathVariable() {
        for (JsParam param : params) {
            if (param.getPathVariableName() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHttpHeader() {
        for (JsParam param : params) {
            if (param.getHttpHeaderName() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRequestParam() {
        for (JsParam param : params) {
            if (param.getRequestParamName() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlainBody() {
        for (JsParam param : params) {
            if (param.isBodyData()) {
                return true;
            }
        }
        return false;
    }

    public JsParam getRequestBodyParam() {
        for (JsParam param : params) {
            if (param.isBodyData()) {
                return param;
            }
        }
        return null;
    }
}
