package com.untilled.roadcapture.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Configuration
public class DefaultSwaggerResourceProvider implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        try {
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources("/static/docs/*.yaml");

            if(resources.length == 0) {
                return Collections.emptyList();
            }

            return Arrays.stream(resources).map(resource -> {
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setSwaggerVersion("3");
                swaggerResource.setName(resource.getFilename());
                swaggerResource.setLocation("/docs/" + resource.getFilename());
                return swaggerResource;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("DefaultSwaggerResourceProvider error", e);
            return Collections.emptyList();
        }
    }
}
