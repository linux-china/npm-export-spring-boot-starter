package org.mvnsearch.boot.npm.export;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.mvnsearch.boot.npm.export.generator.ControllerJavaScriptStubGenerator;
import org.mvnsearch.boot.npm.export.generator.PackageJsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * npm export Controller
 *
 * @author linux_china
 */
@RestController
public class NpmExportController {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment env;

    @GetMapping(value = "/npm/{*packageName}", produces = {"application/tar+gzip"})
    public byte[] npmPackage(@PathVariable("packageName") String packageName, ServerWebExchange exchange) throws IOException {
        if (packageName.startsWith("/")) {
            packageName = packageName.substring(1);
        }
        String controllerClassName = packageName.substring(packageName.lastIndexOf("/") + 1);
        Object controllerBean = getControllerBean(controllerClassName);
        if (controllerBean != null) {
            String uri = exchange.getRequest().getURI().toString();
            String baseUrl = uri.substring(0, uri.indexOf("/", 9));
            String version = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(bos);
            TarArchiveOutputStream tgzOut = new TarArchiveOutputStream(gzOut);
            //package.json
            PackageJsonGenerator jsonGenerator = new PackageJsonGenerator(packageName, version);
            jsonGenerator.addContext("description", "npm package to call " + controllerClassName + " REST API from " + env.getProperty("spring.application.name") + " Spring Boot App");
            addBinaryToTarGz(tgzOut, controllerClassName + "/package.json", jsonGenerator.generate().getBytes(StandardCharsets.UTF_8));
            //index.js
            ControllerJavaScriptStubGenerator jsGenerator = new ControllerJavaScriptStubGenerator(controllerBean.getClass());
            addBinaryToTarGz(tgzOut, controllerClassName + "/index.js", jsGenerator.generate(baseUrl).getBytes(StandardCharsets.UTF_8));
            tgzOut.finish();
            tgzOut.close();
            gzOut.close();
            return bos.toByteArray();
        } else {
            return new byte[]{};
        }
    }

    public Object getControllerBean(String controllerClassName) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            if (bean.getClass().getSimpleName().equals(controllerClassName)) {
                return bean;
            }
        }
        return null;
    }

    @GetMapping(value = "/jsmodule/{controllerName}", produces = "application/javascript")
    public String jsModule(@PathVariable("controllerName") String controllerName) {
        return "good";
    }

    public void addBinaryToTarGz(TarArchiveOutputStream tgzOut, String name, byte[] content) throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(name);
        entry.setSize(content.length);
        tgzOut.putArchiveEntry(entry);
        tgzOut.write(content);
        tgzOut.closeArchiveEntry();
    }
}
