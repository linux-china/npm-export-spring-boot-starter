package org.mvnsearch.boot.npm.export.generator;

import org.junit.jupiter.api.Test;

/**
 * PackageJsonGenerator test
 *
 * @author linux_china
 */
public class PackageJsonGeneratorTest {
    private final PackageJsonGenerator generator = new PackageJsonGenerator("@UserService/UserController", "2020.3.2");

    @Test
    public void testGenerate() throws Exception {
        generator.addContext("description", "package to call REST API");
        System.out.println(generator.generate());
    }
}
