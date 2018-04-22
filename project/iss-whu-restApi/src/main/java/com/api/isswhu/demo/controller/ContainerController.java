package com.api.isswhu.demo.controller;

import com.api.isswhu.demo.domain.mysql.Imageinfo;
import com.api.isswhu.demo.domain.other.RespCode;
import com.api.isswhu.demo.domain.other.ResponData;
import com.api.isswhu.demo.util.ResponseUtil;
import com.spotify.docker.client.exceptions.DockerException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/isswhu/container")
public class ContainerController extends BaseController {

    @RequestMapping(value = "/createContainer", method = RequestMethod.GET)
    public ResponData createContainer(@RequestParam(value = "name", required = true) String name) {
        if (StringUtils.isBlank(name)) {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }
        try {
            String containerId = dockerService.createContainer(name);
            return ResponseUtil.ok(containerId);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseUtil.error();
    }


    @RequestMapping(value = "/removeContainer", method = RequestMethod.GET)
    public ResponData removeContainer(@RequestParam(value = "containerId", required = true) String containerId) {
        if (StringUtils.isBlank(containerId)) {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }
        try {
            dockerService.removeContainer(containerId);
            return ResponseUtil.ok(containerId);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseUtil.error();
    }
}
