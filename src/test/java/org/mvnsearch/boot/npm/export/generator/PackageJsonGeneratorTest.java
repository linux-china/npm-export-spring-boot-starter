package org.mvnsearch.boot.npm.export.generator;

import org.junit.jupiter.api.Test;

/**
 * PackageJsonGenerator test
 *
 * @author linux_china
 */
public class PackageJsonGeneratorTest {
    private final PackageJsonGenerator generator = new PackageJsonGenerator();

    @Test
    public void testGenerate() throws Exception {
        generator.addContext("packageName", "@UserService/UserController");
        System.out.println(generator.generate());
    }
}
