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
@RequestMapping("/isswhu/image")
public class ImageInfoController extends BaseController {
    
//    @RequestMapping(value = "/insertService", method = RequestMethod.GET)
//    @ResponseBody
//    public String insertService(@RequestParam(value = "serviceId", required = false, defaultValue = "21312") String serviceId) {
//        Serviceinfo serviceinfo = new Serviceinfo();
//        serviceinfo.setServiceid(serviceId);
//        serviceinfo.setImagename("RKd");
//        serviceinfo.setMonitorid("232");
//        serviceinfo.setReplica(21);
//        serviceinfoDbService.insertService(serviceinfo);
//        return "插入成功";
//    }
    
    /**
     * 创建 镜像
     * @param dockerFilePath docker路径
     * @param imageName 创建的镜像名
     * @param tag 标志: 默认是 latest
     * @return
     */
    @RequestMapping(value = "/createImage", method = RequestMethod.GET)
    public ResponData createImage(@RequestParam(value = "dfpath", required = true) String dockerFilePath, @RequestParam(value = "imageName", required = true) String imageName,@RequestParam(value = "tag", defaultValue = "latest") String tag) {
        if (StringUtils.isBlank(imageName) || StringUtils.isBlank(dockerFilePath)) {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }
        if (!imageName.toLowerCase().equals(imageName)) {
            return ResponseUtil.badResult(RespCode.PARAM_LOWCASE_ERROR);
        }
        List<Imageinfo> imageInfoByImageName = imageInfoDBService.getImageInfoByImageName(getNameByImageNameAndTag(imageName,tag ));
        if (imageInfoByImageName != null && imageInfoByImageName.size() != 0) {
            return ResponseUtil.badResult(RespCode.PARAM_IMAGE_DUPLICATE);
        }
        try {
            String imageId = dockerService.createImage(dockerFilePath, imageName,tag);
            Imageinfo imageinfo = new Imageinfo();
            if (!StringUtils.isBlank(imageId)) {
                imageinfo.setFilepath(dockerFilePath);
                imageinfo.setImageid(imageId);
                imageinfo.setImagename(imageName);
                imageinfo.setTag(tag);
                //插入数据库
                imageInfoDBService.insertImageInfo(imageinfo);
                return ResponseUtil.ok(imageinfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    
    @RequestMapping(value = "/removeImage", method = RequestMethod.GET)
    public ResponData removeImage(@RequestParam(value = "imageName", required = true) String imageName,@RequestParam(value = "tag", required = true, defaultValue = "latest") String tag) {
        try {
            if (StringUtils.isBlank(imageName)) {
                return ResponseUtil.badResult(RespCode.PARAM_ERROR);
            }
            dockerService.deleteImageByNameAndTag(imageName,tag);
            //删除数据库对应的数据
            imageInfoDBService.deleteImageByName(getNameByImageNameAndTag(imageName, tag));
            return ResponseUtil.ok();
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseUtil.error();
    }
    
    @RequestMapping(value = "/getByImageName", method = RequestMethod.GET)
    public ResponData getSingleServiceInfo(@RequestParam(value = "imageName", required = true) String imageName,@RequestParam(value = "tag", required = true ,defaultValue = "latest") String tag) {
        if (StringUtils.isBlank(imageName)) {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }
        List<Imageinfo> imageInfoByImageName = imageInfoDBService.getImageInfoByImageName(getNameByImageNameAndTag(imageName, tag));
        return ResponseUtil.ok(imageInfoByImageName);
    }
    
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponData getOne() {
        
        return ResponseUtil.ok("imageOk");
    }
}
