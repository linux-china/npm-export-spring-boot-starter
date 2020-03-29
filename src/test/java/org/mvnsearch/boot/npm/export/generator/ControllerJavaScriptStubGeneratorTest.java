package org.mvnsearch.boot.npm.export.generator;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.mvnsearch.boot.npm.export.demo.UserController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * ControllerJavaScriptStubGenerator test
 *
 * @author linux_china
 */
public class ControllerJavaScriptStubGeneratorTest {
    private ControllerJavaScriptStubGenerator generator = new ControllerJavaScriptStubGenerator(UserController.class);

    @Test
    public void testParserController() {
        System.out.println(generator);
    }

    @Test
    public void testGenerateJsModule() throws Exception {
        String jsCode = generator.generate("http://localhost:8080");
        FileOutputStream fos = new FileOutputStream(new File("src/test/nodejs/demo/UserController.js"));
        IOUtils.copy(new ByteArrayInputStream(jsCode.getBytes(StandardCharsets.UTF_8)), fos);
        fos.close();
        System.out.println(jsCode);
    }

    @Test
    public void testJsMethodGenerate() throws Exception {
        Method method = UserController.class.getMethod("findNickById2", Integer.class);
        JsHttpStubMethod jsMethod = generator.generateMethodStub(method);
        System.out.println(jsMethod);
    }

    @Test
    public void testOutputJsCode() throws Exception {
        Method method = UserController.class.getMethod("findNickById", Integer.class);
        JsHttpStubMethod jsMethod = generator.generateMethodStub(method);
        System.out.println(generator.toJsCode(jsMethod, "  "));
    }
}
