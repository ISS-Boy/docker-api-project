package com.api.isswhu.demo.controller;

import com.api.isswhu.demo.domain.mysql.Imageinfo;
import com.api.isswhu.demo.domain.mysql.Serviceinfo;
import com.api.isswhu.demo.domain.other.RespCode;
import com.api.isswhu.demo.domain.other.ResponData;
import com.api.isswhu.demo.util.ResponseUtil;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ServiceCreateResponse;
import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/isswhu/service")
public class ServiceInfoController extends BaseController {
    
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
    
    @RequestMapping(value = "/getAllServices", method = RequestMethod.GET)
    public ResponData getAllServices() {
        try {
            List<Service> allServices = dockerService.getAllServices();
            return ResponseUtil.ok(allServices);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    
    /**
     * 查询某个service 某种状态的 container的容器数量
     * @param serviceName
     * @param state
     * @return
     */
    @RequestMapping(value = "/getAllTaskByState", method = RequestMethod.GET)
    public ResponData getAllTask(@RequestParam(value = "serviceName") String serviceName,@RequestParam(value = "state",defaultValue = "running") String state) {
        try {
            List<Task> allTask = dockerService.getAllTask(serviceName,state);
            
            return ResponseUtil.ok(allTask);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    
    @RequestMapping(value = "/getAllNodes", method = RequestMethod.GET)
    public ResponData getAllNodes() {
        try {
            List<Node> allNodes = dockerService.getAllNodes();
            return ResponseUtil.ok(allNodes);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    @RequestMapping(value = "/getSingleServiceInfo", method = RequestMethod.GET)
    public ResponData getSingleServiceInfo(@RequestParam(value = "serviceName", required = false) String serviceName,@RequestParam(value = "serviceId", required = false) String serviceId, @RequestParam(value = "monitorId", required = false) String monitorId) {
        Map<String,String> hash = new HashMap<>();
        if (!StringUtils.isBlank(serviceId)) {
            hash.put("serviceId", serviceId);
        } else if (!StringUtils.isBlank(monitorId)) {
            hash.put("monitorId", monitorId);
        }else if (!StringUtils.isBlank(serviceName)) {
            hash.put("serviceName", serviceName);
        } else {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }

//        if (StringUtils.isBlank(imageName)) {
//            hash.put("imageName", imageName);
//        }
        
        Serviceinfo serviceByParam = serviceinfoDbService.getServiceByParam(hash);
        if (serviceByParam != null) {
            return ResponseUtil.ok(serviceByParam);
        }
        return ResponseUtil.error();
    }
    
    @RequestMapping(value = "/selectByImageName", method = RequestMethod.GET)
    public ResponData selectByImageName(@RequestParam(value = "imageName", required = false) String imageName) {
        if (!StringUtils.isBlank(imageName)) {
            List<Serviceinfo> serviceByImageName = serviceinfoDbService.getServiceByImageName(imageName);
            if (serviceByImageName != null && serviceByImageName.size() > 0) {
                return ResponseUtil.ok(serviceByImageName);
            }
        } else {
            return ResponseUtil.badResult(RespCode.PARAM_ERROR);
        }

//        if (StringUtils.isBlank(imageName)) {
//            hash.put("imageName", imageName);
//        }
        return ResponseUtil.error();
    }
    
    

    
    /**
     * 创建service
     * @param monitorId
     * @param imageName
     * @param seriviceName
     * @param replicaNum
     * @param tag
     * @return
     */
    @RequestMapping(value = "/createService", method = RequestMethod.GET)
    public ResponData createService(@RequestParam(value = "monitorId") String monitorId, @RequestParam(value = "imageName") String imageName, @RequestParam(value = "serviceName") String seriviceName, @RequestParam(value = "replicaNum",defaultValue = "4") int replicaNum,@RequestParam(value = "tag", required = true, defaultValue = "latest") String tag) {
        try {
            if (StringUtils.isBlank(imageName) ||StringUtils.isBlank(monitorId) ||StringUtils.isBlank(seriviceName)) {
                return ResponseUtil.badResult(RespCode.PARAM_ERROR);
            }
            ServiceCreateResponse service = dockerService.createService(seriviceName, imageName, replicaNum, tag);
            if (service.id() != null) {
                //<editor-fold desc="历史数据">
                //            String sql = "insert into serviceInfo (serviceId,serviceName,monitorId,imageName,replica) values ( '"
//                                 +response.id().substring(0,5)+"','"
//                                 +seriviceName+"','"
//                                 +monitorId+"','"
//                                 +imageName+"','"
//                                 +replicaNum+"')";
//            dbHandler.setPst(sql);
//            dbHandler.pst.execute(sql);
//            dbHandler.close();
                //</editor-fold>
                Serviceinfo serviceinfo = new Serviceinfo();
                serviceinfo.setServiceid(service.id());
                serviceinfo.setServicename(seriviceName);
                serviceinfo.setMonitorid(monitorId);
                serviceinfo.setReplica(replicaNum);
                //这里镜像名称最好是全称吧
                serviceinfo.setImagename(getNameByImageNameAndTag(imageName, tag));
//                serviceinfo.setPort(port + "");
                serviceinfoDbService.insertService(serviceinfo);
                return ResponseUtil.ok(service.id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    
    @RequestMapping(value = "/deleteServiceByName", method = RequestMethod.GET)
    public ResponData deleteServiceByName(@RequestParam(value = "serviceName") String seriviceName) {
        try {
            dockerService.deleteServiceByName(seriviceName);
            //删除数据库
            serviceinfoDbService.deleteByServiceName(seriviceName);
            return ResponseUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseUtil.error();
    }
    
    
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponData getOne() {
        
        return ResponseUtil.ok("serviceOk");
    }
}
