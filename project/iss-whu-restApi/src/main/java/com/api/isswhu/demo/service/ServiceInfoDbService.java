package com.api.isswhu.demo.service;

import com.api.isswhu.demo.domain.mysql.Serviceinfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServiceInfoDbService extends BaseService {
    /**
     * 对数据库进行添加
     * @param serviceinfo
     */
    public void insertService(Serviceinfo serviceinfo) {
        serviceinfoMapper.insert(serviceinfo);
    }
    
    /**
     * 对数据库进行删减
     * @param name
     */
    public void deleteByServiceName(String name) {
        serviceinfoMapper.deleteByServiceName(name);
    }
    /**
     *
     * @param param
     * @return
     */
    public Serviceinfo getServiceByParam(Map<String ,String > param) {
        Object serviceId = param.get("serviceId") ;
        Object monitorId = param.get("monitorId");
        Object serviceName = param.get("serviceName");
        if (serviceId != null && !StringUtils.isBlank(serviceId.toString())) {
            Serviceinfo serviceinfo = serviceinfoMapper.selectByPrimaryKey(serviceId.toString());
            return serviceinfo;
        }
        if (monitorId != null && !StringUtils.isBlank(monitorId.toString())) {
            Serviceinfo serviceinfo = serviceinfoMapper.selectByMonitorId(monitorId.toString());
            return serviceinfo;
        }
        if (serviceName != null && !StringUtils.isBlank(serviceName.toString())) {
            Serviceinfo serviceinfo = serviceinfoMapper.selectByServiceName(serviceName.toString());
            return serviceinfo;
        }
        return null;
    }
    
    public List<Serviceinfo> getServiceByImageName(String imageName) {
        List<Serviceinfo> serviceinfos = serviceinfoMapper.selectByImageName(imageName);
        return serviceinfos;
    }
}
