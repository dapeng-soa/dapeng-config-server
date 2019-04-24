package com.github.dapeng.web.config;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.dto.HostDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.json.OptimizedMetadata;
import com.github.dapeng.openapi.cache.ServiceCache;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.vo.RouteInitData;
import com.github.dapeng.vo.ServiceInfo;
import com.github.dapeng.vo.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huyj
 * @Created 2019-03-19 21:39
 */
@RestController
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/api")
public class RouteRestController {

    @Autowired
    HostRepository hostRepository;

    /**
     * 加载路由 初始数据
     *
     * @return
     */
    @GetMapping(value = "/route/loadData")
    public ResponseEntity<?> loadInitData() {
        //ServiceCache.getServices();
        RouteInitData routeInitData = loadServiceInfo();
        if (routeInitData != null) {
            routeInitData.setHostList(loadHostList());
        }

        if (routeInitData != null) {
            routeInitData.setStoreList(loadStoreList());
        }

        return ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG, routeInitData));
    }


    private RouteInitData loadServiceInfo() {
        RouteInitData routeInitData = null;
        Map<String, OptimizedMetadata.OptimizedService> stringOptimizedServiceMap = ServiceCache.getServices();
        if (stringOptimizedServiceMap != null && !stringOptimizedServiceMap.isEmpty()) {
            List<ServiceInfo> serviceInfoList = new ArrayList<>(64);
            Set<String> versionList = new HashSet<>(64);
            routeInitData = new RouteInitData();
            stringOptimizedServiceMap.forEach((serviceName, value) -> {
                String version = serviceName.split(":")[1];
                String serviceFullName = value.getService().getNamespace() + "." + value.getService().name;
                List<String> methods = value.getService().getMethods().stream().map(Method::getName).collect(Collectors.toList());
                methods.add("getServiceMetadata");

                ServiceInfo serviceInfo = new ServiceInfo(serviceName, serviceFullName, version, methods);

                versionList.add(version);
                serviceInfoList.add(serviceInfo);
            });
            routeInitData.setServiceInfoList(serviceInfoList);
            routeInitData.setVersionList(versionList);
        }
        return routeInitData;
    }

    private List<HostDto> loadHostList() {
        List<HostDto> hostList = new ArrayList<>();

        List<THost> hostListTemp = hostRepository.findAll();
        for (THost tHost : hostListTemp) {
            //添加无效地址
            HostDto hostDto = new HostDto();
            hostDto.setIp(IPUtils.transferIp(tHost.getIp()));
            hostDto.setName(tHost.getName());
            hostList.add(hostDto);
        }

        //添加无效地址
        HostDto hostDto = new HostDto();
        hostDto.setLabels("1.1.1.1");
        hostDto.setIp("1.1.1.1");
        hostDto.setName("无效地址");
        hostDto.setRemark("无效地址");
        hostList.add(hostDto);
        return hostList;
    }

    private List<Store> loadStoreList() {
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("170XXX001", "测试店1"));
        stores.add(new Store("170XXX002", "测试店2"));
        stores.add(new Store("170XXX003", "测试店3"));
        stores.add(new Store("170XXX004", "测试店4"));
        stores.add(new Store("170XXX005", "测试店5"));
        stores.add(new Store("170XXX006", "测试店6"));
        stores.add(new Store("170XXX007", "测试店7"));
        stores.add(new Store("170XXX008", "测试店8"));
        return stores;
    }
}
