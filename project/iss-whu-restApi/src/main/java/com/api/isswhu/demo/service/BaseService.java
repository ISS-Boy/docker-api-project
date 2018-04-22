package com.api.isswhu.demo.service;

import com.api.isswhu.demo.constant.DockerConfigConstant;
import com.api.isswhu.demo.constant.DockerConstant;
import com.api.isswhu.demo.dao.ImageinfoMapper;
import com.api.isswhu.demo.dao.ServiceinfoMapper;
import com.spotify.docker.client.DockerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 这个是总的service 框架
 */
@Service
public class BaseService {
    
    @Autowired
    protected ServiceinfoMapper serviceinfoMapper;
    
    @Autowired
    protected ImageinfoMapper imageinfoMapper;
    
    @Autowired
    protected DockerClient dockerClient;
    
    @Autowired
    protected DockerConstant dockerConstant;
    
    @Autowired
    protected DockerConfigConstant dockerConfigConstant;
    
    /**
     * 根据镜像名字 获取完整的带仓库的名字(公用仓库)
     * imageName => rkd1/imageName:tag
     *
     * @param imageName
     * @param tag
     * @return
     */
    protected String getNameByImageName(String imageName, String tag) {
        //docker hub 上的仓库
//        return  dockerConfigConstant.getUserName() + "/" + imageName + ":" + tag;
        /**
         * 本地私有仓库
         */
        return dockerConfigConstant.getRegistryIp()+":"+dockerConfigConstant.getRegistryPort()+"/"+ imageName + ":" + tag;
    }
    
}
