package org.mvnsearch.boot.npm.export.generator;

import org.junit.jupiter.api.Test;
import org.mvnsearch.boot.npm.export.demo.UserController;

import java.lang.reflect.Method;

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
    public void testGenerateJsModule() {
        System.out.println(generator.generate("http://localhost:8080"));
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
        System.out.println(generator.toJsCode(jsMethod));
    }
}
