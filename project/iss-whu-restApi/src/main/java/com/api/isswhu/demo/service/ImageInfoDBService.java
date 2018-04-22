package com.api.isswhu.demo.service;

import com.api.isswhu.demo.domain.mysql.Imageinfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageInfoDBService extends BaseService {
    
    public void insertImageInfo(Imageinfo imageinfo) {
        //对imageName 做一下处理
        String imagename = imageinfo.getImagename();
        if (StringUtils.isBlank(imagename)) {
            //TODO: 这里要抛出一个异常
            return;
        }
        String[] split = imagename.split(":");
        if (split.length != 2) {
            imageinfo.setImagename(getNameByImageName(imageinfo.getImagename(), imageinfo.getTag()));
        }
        imageinfoMapper.insert(imageinfo);
    }
    
    public void deleteImageByName(String name) {
        imageinfoMapper.deleteByImageName(name);
    }
    
    public List<Imageinfo> getImageInfoByImageName(String imageName) {
        return imageinfoMapper.selectByImageName(imageName);
    }
}
