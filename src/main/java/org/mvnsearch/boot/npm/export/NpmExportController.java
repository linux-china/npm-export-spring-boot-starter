package org.mvnsearch.boot.npm.export;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.mvnsearch.boot.npm.export.generator.ControllerJavaScriptStubGenerator;
import org.mvnsearch.boot.npm.export.generator.PackageJsonGenerator;
import org.mvnsearch.boot.npm.export.generator.TypeScriptDeclarationGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Object controllerBean = getControllerBean(controllerClassName, packageName);
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
            //index.d.ts
            TypeScriptDeclarationGenerator tsGenerator = new TypeScriptDeclarationGenerator(controllerBean.getClass());
            addBinaryToTarGz(tgzOut, controllerClassName + "/index.d.ts", tsGenerator.generate().getBytes(StandardCharsets.UTF_8));
            tgzOut.finish();
            tgzOut.close();
            gzOut.close();
            return bos.toByteArray();
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return new byte[]{};
        }
    }

    public Object getControllerBean(String controllerClassName, String packageName) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            NpmPackage npmPackage = beanClass.getAnnotation(NpmPackage.class);
            if (npmPackage != null && npmPackage.version().equals(packageName)) {
                return bean;
            } else if (beanClass.getSimpleName().equals(controllerClassName)) {
                return bean;
            }
        }
        return null;
    }

    @GetMapping(value = "/npm/packages", produces = "text/markdown")
    public String npmPackages(ServerWebExchange exchange) {
        List<String> controllers = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            NpmPackage npmPackage = beanClass.getAnnotation(NpmPackage.class);
            if (npmPackage != null) {
                controllers.add(npmPackage.value());
            }
        }
        String uri = exchange.getRequest().getURI().toString();
        String baseUrl = uri.substring(0, uri.indexOf("/", 9));
        StringBuilder builder = new StringBuilder();
        builder.append("# NPM Packages\n\n");
        builder.append("```json\n");
        builder.append("\"dependencies\": {\n");
        for (String controller : controllers) {
            builder.append("  \"" + controller + "\": \"" + baseUrl + "/npm/" + controller + "\",\n");
        }
        builder.append("}\n");
        builder.append("```\n\n");
        builder.append("# How to use?\n" +
                "\n" +
                "* Include dependency in your package.json and run \"yarn install\"\n" +
                "\n" +
                "```\n" +
                " \"dependencies\": {\n" +
                "    \"@UserService/UserController\": \"http://localhost:8080/npm/@UserService/UserController\"\n" +
                "  }\n" +
                "```\n" +
                "\n" +
                "* Call service api in your JS code:\n" +
                "\n" +
                "```\n" +
                "const userController = require(\"@UserService/UserController\").setBaseUrl(\"http://localhost:8080\");\n" +
                "\n" +
                "(async () => {\n" +
                "    let nick = await userController.findNickById(1);\n" +
                "    console.log(nick);\n" +
                "})()\n" +
                "```");
        return builder.toString();
    }

    public void addBinaryToTarGz(TarArchiveOutputStream tgzOut, String name, byte[] content) throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(name);
        entry.setSize(content.length);
        tgzOut.putArchiveEntry(entry);
        tgzOut.write(content);
        tgzOut.closeArchiveEntry();
    }
}
