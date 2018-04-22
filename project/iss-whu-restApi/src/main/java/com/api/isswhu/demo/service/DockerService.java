package com.api.isswhu.demo.service;

import com.api.isswhu.demo.domain.mysql.Serviceinfo;
import com.api.isswhu.demo.util.NumUtil;
import com.google.common.io.Resources;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import com.spotify.docker.client.messages.Network;
import com.spotify.docker.client.messages.swarm.*;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DockerService extends BaseService {

    /**
     * 根据dockerFile 创建 image 镜像
     *
     * @param dockerfilepath 绝对路径
     * @param imageName
     * @param tagName        tag
     * @return
     * @throws Exception
     */
    public String createImage(String dockerfilepath, String imageName, String tagName) throws Exception {

        final AtomicReference<String> imageIdFromMessage = new AtomicReference<>();
        final String returnedImageId = dockerClient.build(Paths.get(dockerfilepath), imageName, new ProgressHandler() {
            @Override
            public void progress(ProgressMessage message) throws DockerException {
                final String imageId = message.buildImageId();
                if (imageId != null) {
                    imageIdFromMessage.set(imageId);
                }
            }
        });
//        String tag = dockerConfigConstant.getUserName() + "/" + imageName + ":" + tagName;
        dockerClient.tag(imageName, getNameByImageName(imageName, tagName));
        dockerClient.push(getNameByImageName(imageName, tagName));

        return returnedImageId;
    }

    /**
     * 创建service
     *
     * @param seriviceName
     * @param imageName
     * @param replicaNum
     * @return 是否创建成功
     * @throws Exception
     */
    public ServiceCreateResponse createService(String seriviceName, String imageName, int replicaNum, String tag) throws Exception {
//        int ServicePort = NumUtil.getRandomPort();
//        while (true) {
//            Serviceinfo serviceinfo = serviceinfoMapper.selectByPort(ServicePort + "");
//            if (serviceinfo == null) {
//                break;
//            }
//            ServicePort = NumUtil.getRandomPort();
//        }
//        String[] split = nestPort.split("-");
        final ServiceSpec spec = createServiceSpec(seriviceName, getNameByImageName(imageName, tag), replicaNum);
        final ServiceCreateResponse response = dockerClient.createService(spec);


        return response;
    }

    /**
     * 创建一个新的容器同时，部署到 网络上
     *
     * @param containerName
     * @return
     */
    public String createContainer(String containerName) throws DockerException, InterruptedException {
        /**
         * NetworkConfig.builder()
         *                                                        .name(networkName)
         *                                                        .ipam(ipamToCreate)
         *                                                        .build();
         */

        final ContainerConfig containerConfig = ContainerConfig.builder()
                .image(dockerConfigConstant.getImageName())
                //TODO: 这里只是测试，因为镜像是测试的，到实际的时候，用正常的镜像，这个命令就无用了
                .cmd("sh", "-c", "while :; do sleep 1; done")//make sure the container is doing something
                .build();
        final ContainerCreation creation = dockerClient.createContainer(containerConfig, containerName);
//        System.out.println(creation.id());
        final String id = creation.id();
        dockerClient.startContainer(id);
//        System.out.println("成功");
        dockerClient.connectToNetwork(id, dockerConfigConstant.getNetworkID());
        Network network = dockerClient.inspectNetwork(dockerConfigConstant.getNetworkID());
//        System.out.println(network.containers().size());

        return id;
    }

    /**
     * 指定containerId 删除对应的container
     *
     * @param containerId
     * @throws DockerException
     * @throws InterruptedException
     */
    public void removeContainer(String containerId) throws DockerException, InterruptedException {
        dockerClient.killContainer(containerId);
        dockerClient.removeContainer(containerId);

    }

    /**
     * 根据serviceName 删除service
     *
     * @param serviceName
     * @return
     */
    public Boolean deleteServiceByName(String serviceName) throws Exception {
        //<editor-fold desc="历史版本">
        //        Boolean sucessOrNot = false;
//        try {
//            client.removeService(serviceName);
//            String sql = "delete from serviceInfo where serviceName = '"+serviceName +"'";
//            dbHandler.setPst(sql);
//            dbHandler.pst.execute(sql);
//            dbHandler.close();
//            sucessOrNot = true;
//        }catch (Exception e){
//            sucessOrNot = false;
//            e.printStackTrace();
//        }
        //</editor-fold>
        dockerClient.removeService(serviceName);

        return true;

    }

    public Map<String, String> getDockerConfig() {
        Map<String, String> config = dockerConstant.getConfig();
        System.out.println(config.get("url"));
        System.out.println(config.get("userName"));
        System.out.println(config.get("pswd"));
        return config;
    }

    public Map<String, String> getDockerConfigConst() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", dockerConfigConstant.getUserName());
        map.put("pswd", dockerConfigConstant.getPswd());
        map.put("url", dockerConfigConstant.getUrl());
        map.put("visualizer", dockerConfigConstant.getVisualizerName());
        System.out.println("=====================!");
        System.out.println(map.get("url"));
        System.out.println(map.get("userName"));
        System.out.println(map.get("pswd"));
        System.out.println("=====================!");
        return map;
    }

    /**
     * 删除镜像
     *
     * @param imageName
     * @throws DockerException
     * @throws InterruptedException
     */
    public void deleteImageByNameAndTag(String imageName, String tag) throws DockerException, InterruptedException {
        dockerClient.removeImage(imageName);
        dockerClient.removeImage(getNameByImageName(imageName, tag));
    }

    /**
     * 获取所有的Service
     *
     * @return
     * @throws Exception
     */
    public List<com.spotify.docker.client.messages.swarm.Service> getAllServices() throws Exception {
        List<com.spotify.docker.client.messages.swarm.Service> services = dockerClient.listServices();
        for (int i = 0; i < services.size(); i++) {
            com.spotify.docker.client.messages.swarm.Service service = services.get(i);
            if (service.spec().name().equals(dockerConfigConstant.getVisualizerName())) {
                services.remove(i);
                break;
            }
        }
        if (services.size() == 0) {
            return null;
        }
        return services;
    }

    /**
     * 获取某个service 中某个state的正在运行的容器的数量
     *
     * @param serviceName service 名字
     * @param state       状态 running -  shutdown
     * @return
     * @throws Exception
     */
    public List<Task> getAllTask(String serviceName, String state) throws Exception {

        //running-shutdown-ready
        List<Task> tasks = dockerClient.listTasks(Task.find().serviceName(serviceName).desiredState(state).build());
        return tasks;
    }

    /**
     * 获取所有的Service
     *
     * @return
     * @throws Exception
     */
    public List<Node> getAllNodes() throws Exception {
        List<Node> nodes = dockerClient.listNodes();
        return nodes;
    }

    public ServiceSpec createServiceSpec(String serviceName, String imageName, int replicaNum) {

        return createServiceSpec(serviceName, imageName, replicaNum, new HashMap<String, String>());
    }

    private ServiceSpec createServiceSpec(String serviceName, String imageName, int replicaNum,
                                          final Map<String, String> labels) {

        try {
            final TaskSpec taskSpec = TaskSpec
                    .builder()
                    .containerSpec(ContainerSpec.builder().image(imageName).build())
                    .build();

            final ServiceMode serviceMode = ServiceMode.withReplicas(replicaNum);

            final EndpointSpec endpointSpec = EndpointSpec
                    .builder()
                    .build();
            //不在暴露端口，因为内部走的是 虚拟网络流程
//                    .ports(PortConfig.builder().protocol("tcp").targetPort(nestPort).publishedPort(nestPort).build())
//            final EndpointSpec endpointSpec = EndpointSpec
//                                                      .builder()
//                                                      .ports(PortConfig.builder().protocol("tcp").targetPort(80).publishedPort(Sport).build(),PortConfig.builder().protocol("tcp").targetPort(nestPort).publishedPort(nestPort).build())
//                                                      .build();


            return ServiceSpec.builder().name(serviceName).taskTemplate(taskSpec)
                    .mode(serviceMode)
                    .endpointSpec(endpointSpec)
                    .networks(NetworkAttachmentConfig.builder().target(dockerConfigConstant.getNet()).build())
                    .labels(labels)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    //创建负载均衡网络
    private Boolean createNetwork(String driver, String name) {
        try {

            NetworkConfig.Builder networkConfigBuilder =
                    NetworkConfig.builder()
                            .driver(driver)//overlay
                            .name(name);

            final NetworkCreation networkCreation = dockerClient.createNetwork(networkConfigBuilder.build());
            if (networkCreation.id().equals(null)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private final Path getResource(String name) throws URISyntaxException {
        // Resources.getResources(...).getPath() does not work correctly on windows,
        // hence this workaround.  See: https://github.com/spotify/docker-client/pull/780
        // for details
        return Paths.get(Resources.getResource(name).toURI());
    }


}
