package org.mvnsearch.boot.npm.export;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * npm export auto configuration
 *
 * @author linux_china
 */
@Configuration
public class NpmExportAutoConfiguration {

    @Bean
    public NpmExportController npmExportController() {
        return new NpmExportController();
    }
}
