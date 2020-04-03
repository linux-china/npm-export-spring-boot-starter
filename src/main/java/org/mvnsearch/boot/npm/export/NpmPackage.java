package org.mvnsearch.boot.npm.export;

import java.lang.annotation.*;

/**
 * npm package annotation
 *
 * @author linux_china
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NpmPackage {

    String value() default "";

    String version() default "";
}
