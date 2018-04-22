package com.api.isswhu.demo.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "docker.config")
public class DockerConfigConstant {
    private String url;

    private String userName;

    private String pswd;
    private String networkID;
    private String imageName;
    private String visualizerName;
    private String registryPort;
    private String registryIp;
    private String net;

    public String getImageName() {
        return imageName;
    }

    public String getNetworkID() {
        return networkID;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getNet() {
        return net;
    }
    
    public void setNet(String net) {
        this.net = net;
    }
    
    public String getVisualizerName() {
        return visualizerName;
    }
    
    public void setVisualizerName(String visualizerName) {
        this.visualizerName = visualizerName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
//    public String getNetwork() {
//        return network;
//    }
//
//    public void setNetwork(String network) {
//        this.network = network;
//    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPswd() {
        return pswd;
    }
    
    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
    
    public String getRegistryPort() {
        return registryPort;
    }
    
    public void setRegistryPort(String registryPort) {
        this.registryPort = registryPort;
    }
    
    public String getRegistryIp() {
        return registryIp;
    }
    
    public void setRegistryIp(String registryIp) {
        this.registryIp = registryIp;
    }
}
