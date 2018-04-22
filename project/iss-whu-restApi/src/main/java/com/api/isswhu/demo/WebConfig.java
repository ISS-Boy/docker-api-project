package com.api.isswhu.demo;


import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.auth.FixedRegistryAuthSupplier;
import com.spotify.docker.client.messages.RegistryAuth;
import com.spotify.docker.client.messages.RegistryConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.security.Principal;

import static java.util.Collections.singletonMap;

@Configuration
public class WebConfig {
    
    private String dockerURL;
    /**
     * docker
     * 仓库名
     */
    private String userName;
    /**
     * docker 密码
     */
    private String pswd;
    
    @Value("${docker.config.url}")
    public void setDockerURL(String dockerURL) {
        this.dockerURL = dockerURL;
    }
    
    @Value("${docker.config.userName}")
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Value("${docker.config.pswd}")
    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
    
    @Bean(name = "dockerClient")
    public DockerClient getDockerClient() {
        RegistryAuth registryAuth = RegistryAuth.builder()
                                            .username(userName)
                                            .password(pswd)
                                            .build();
        DockerClient client = DefaultDockerClient.builder()
                                      .uri(URI.create(dockerURL))
                                      .registryAuthSupplier(new FixedRegistryAuthSupplier(
                                              registryAuth, RegistryConfigs.create(singletonMap("", registryAuth))))
                                      .build();
//        DockerClient client = DefaultDockerClient.builder()
//                         .uri(URI.create(dockerURL))
//                         .build();
//        client = DefaultDockerClient.builder()
//                .uri(URI.create("http://172.16.17.182:2376"))
//                .build();
        System.out.println("userName : " + userName + " pswd : " + pswd + " dockURL" + dockerURL);
        return client;
    }
}
