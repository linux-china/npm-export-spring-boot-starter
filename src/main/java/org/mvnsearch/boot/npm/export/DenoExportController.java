package org.mvnsearch.boot.npm.export;

import org.mvnsearch.boot.npm.export.generator.DenoModGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;

/**
 * Deno export Controller
 *
 * @author linux_china
 */
@Controller
public class DenoExportController {
    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @GetMapping(value = "/deno/{controllerClassName}/mod.ts", produces = {"text/x.typescript"})
    @ResponseBody
    public String denoModule(@PathVariable("controllerClassName") String controllerClassName, ServerWebExchange exchange) throws IOException {
        String uri = exchange.getRequest().getURI().toString();
        String baseUrl = uri.substring(0, uri.indexOf("/", 9));
        Object controllerBean = getControllerBean(controllerClassName);
        if (controllerBean != null) {
            DenoModGenerator denoModGenerator = new DenoModGenerator(controllerBean.getClass(), baseUrl);
            return denoModGenerator.generate();
        }
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        return null;
    }

    public Object getControllerBean(String controllerClassName) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            if (beanClass.getSimpleName().equals(controllerClassName)) {
                return bean;
            }
        }
        return null;
    }

}
