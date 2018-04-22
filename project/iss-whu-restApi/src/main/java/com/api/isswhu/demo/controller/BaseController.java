package com.api.isswhu.demo.controller;

import com.api.isswhu.demo.constant.DockerConfigConstant;
import com.api.isswhu.demo.domain.other.ResponData;
import com.api.isswhu.demo.service.DockerService;
import com.api.isswhu.demo.service.ImageInfoDBService;
import com.api.isswhu.demo.service.ServiceInfoDbService;
import com.api.isswhu.demo.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/isswhu")
public class BaseController {
    @Autowired
    protected ServiceInfoDbService serviceinfoDbService;
    
    @Autowired
    protected ImageInfoDBService imageInfoDBService;
    
    @Autowired
    protected DockerService dockerService;
    
    @Autowired
    protected DockerConfigConstant dockerConfigConstant;
    
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    public ResponData getConfig() {
        Map<String, String> dockerConfig = dockerService.getDockerConfig();
        Map<String, String> dockerConfigConst = dockerService.getDockerConfigConst();
        List<Map<String, String>> list = new ArrayList<>();
        list.add(dockerConfig);
        list.add(dockerConfigConst);
        if (dockerConfig != null) {
            return ResponseUtil.ok(list);
        }
        return ResponseUtil.error();
    }
    
    protected String getNameByImageNameAndTag(String imageName, String tag) {
        //docker hub 上的仓库
//        return  dockerConfigConstant.getUserName() + "/" + imageName + ":" + tag;
        /**
         * 本地私有仓库
         */
        return dockerConfigConstant.getRegistryIp()+":"+dockerConfigConstant.getRegistryPort()+"/"+ imageName + ":" + tag;
    }
}
