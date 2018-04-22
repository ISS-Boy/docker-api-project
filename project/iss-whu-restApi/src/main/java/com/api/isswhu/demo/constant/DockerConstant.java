package com.api.isswhu.demo.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
//@ConfigurationProperties(locations = {"classpath:config/myProps.yml"},prefix = "myProps")
@ConfigurationProperties(prefix = "docker")
public class DockerConstant {
    
    private  Map<String, String> config = new HashMap<>();
    
    public Map<String, String> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
