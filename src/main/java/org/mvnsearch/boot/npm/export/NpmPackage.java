package org.mvnsearch.boot.npm.export;

/**
 * npm package annotation
 *
 * @author linux_china
 */
public @interface NpmPackage {

    String name() default "";

    String version() default "1.0.0";
}
