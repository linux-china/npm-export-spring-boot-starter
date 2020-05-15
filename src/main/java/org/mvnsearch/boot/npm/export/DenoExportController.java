package org.mvnsearch.boot.npm.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
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
    private ApplicationContext applicationContext;
    @Autowired
    private Environment env;

    @GetMapping(value = "/deno/{packageName}/mod.ts", produces = {"text/x.typescript"})
    @ResponseBody
    public String denoModule(@PathVariable("packageName") String packageName, ServerWebExchange exchange) throws IOException {
        String uri = exchange.getRequest().getURI().toString();
        String baseUrl = uri.substring(0, uri.indexOf("/", 9));
        return "export function hello() {}";
    }

    @GetMapping(value = "/deno/{packageName}/mod.d.ts", produces = {"text/x.typescript"})
    @ResponseBody
    public String denoModuleDeclare(@PathVariable("packageName") String packageName, ServerWebExchange exchange) throws IOException {
        String uri = exchange.getRequest().getURI().toString();
        String baseUrl = uri.substring(0, uri.indexOf("/", 9));
        return "declare function hello()";
    }

}
