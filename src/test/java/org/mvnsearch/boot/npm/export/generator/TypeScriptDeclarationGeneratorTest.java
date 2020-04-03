package org.mvnsearch.boot.npm.export.generator;

import org.junit.jupiter.api.Test;
import org.mvnsearch.boot.npm.export.demo.UserController;

/**
 * TypeScriptDeclarationGenerator test
 *
 * @author linux_china
 */
public class TypeScriptDeclarationGeneratorTest {

    private TypeScriptDeclarationGenerator generator = new TypeScriptDeclarationGenerator(UserController.class);

    @Test
    public void testGenerateTsDeclare() {
        System.out.println(generator.generate());
    }


}
