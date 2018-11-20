package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.dto.NetworkHostDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TNetwork;
import com.github.dapeng.entity.deploy.TNetworkHost;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.NetworkHostRepository;
import com.github.dapeng.repository.deploy.NetworkRepository;
import com.github.dapeng.util.Check;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.vo.SyncNetworkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/9/27 10:12
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Exception.class)
public class NetworkController {

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    NetworkHostRepository networkHostRepository;

    @Autowired
    HostRepository hostRepository;

    @GetMapping("/deploy-networks")
    public ResponseEntity netWorks(@RequestParam(required = false, defaultValue = "0") int offset,
                                   @RequestParam(required = false, defaultValue = "100000") int limit,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false, defaultValue = "desc") String order,
                                   @RequestParam(required = false, defaultValue = "") String search) {

        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TNetwork> page = networkRepository.findAll((root, query, cb) -> {
            Path<Integer> deleted = root.get("deleted");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.equal(deleted, NORMAL_STATUS));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));

    }


    @PostMapping("/deploy-network/add")
    public ResponseEntity saveNetwork(@RequestBody TNetwork network) {
        try {
            Check.hasChinese(network.getNetworkName(), "网络名称");
            Check.hasChinese(network.getDriver(), "网桥");
            Check.hasChinese(network.getSubnet(), "网段");
            Check.isboolNet(network.getSubnet());
            List<TNetwork> networks = networkRepository.findByNetworkName(network.getNetworkName());
            if (!isEmpty(networks)) {
                throw new Exception("已存在同名网络");
            }
            network.setCreatedAt(DateUtil.now());
            network.setUpdatedAt(DateUtil.now());
            networkRepository.save(network);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/deploy-network/{id}")
    public ResponseEntity netWork(@PathVariable Long id) {
        try {
            TNetwork one = networkRepository.findOne(id);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, one));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, LOAD_DATA_ERROR));
        }
    }

    @PostMapping("/deploy-network/edit/{id}")
    public ResponseEntity editServiceFile(@PathVariable Long id,
                                          @RequestBody TNetwork network) {
        try {
            Check.hasChinese(network.getNetworkName(), "网络名称");
            Check.hasChinese(network.getDriver(), "网桥");
            Check.hasChinese(network.getSubnet(), "网段");
            Check.isboolNet(network.getSubnet());
            TNetwork one = networkRepository.findOne(id);
            one.setNetworkName(network.getNetworkName());
            one.setDriver(network.getDriver());
            one.setOpt(network.getOpt());
            one.setSubnet(network.getSubnet());
            one.setUpdatedAt(DateUtil.now());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 关联unit
     *
     * @return
     */
    @PostMapping("/deploy-network/link-host")
    public ResponseEntity linkHost(@RequestBody NetworkHostDto dto) {
        List<THost> success = new ArrayList<>();
        Map<THost, TNetwork> failds = new HashMap<>();
        try {
            dto.getHostIds().forEach(hid -> {
                // 已绑定的就不管了
                List<TNetworkHost> list = networkHostRepository.findByNetIdAndHostId(dto.getNetId(), hid);
                if (isEmpty(list)) {
                    // 一个节点只能绑定一个网络
                    List<TNetworkHost> byHostId = networkHostRepository.findByHostId(hid);
                    if (isEmpty(byHostId)) {
                        TNetworkHost networkHost = new TNetworkHost();
                        networkHost.setNetId(dto.getNetId());
                        networkHost.setHostId(hid);
                        networkHost.setCreatedAt(DateUtil.now());
                        networkHostRepository.save(networkHost);
                        THost host = hostRepository.findOne(hid);
                        success.add(host);
                    } else {
                        THost host = hostRepository.findOne(hid);
                        TNetwork network = networkRepository.findOne(byHostId.get(0).getNetId());
                        failds.put(host, network);
                    }
                }
            });

            StringBuilder sb = new StringBuilder();
            sb.append("绑定主机成功")
                    .append(success.size()).append("个,")
                    .append("失败").append(failds.size()).append("个");
            if (failds.size() > 0) {
                sb.append(",\n");
                failds.forEach((k, v) -> {
                    sb.append("节点:").append(k.getName()).append("已绑定网络:").append(v.getNetworkName()).append("\n");
                });
            }
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 取消关联unit
     *
     * @return
     */
    @PostMapping("/deploy-network/unlink-host")
    public ResponseEntity unLinkUnit(@RequestBody NetworkHostDto dto) {
        try {
            dto.getHostIds().forEach(hid -> {
                networkHostRepository.deleteByNetIdAndHostId(dto.getNetId(), hid);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "批量取消关联成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * @param netId
     * @return
     */
    @GetMapping("/deploy-network-hosts/{netId}")
    public ResponseEntity linkedHosts(@PathVariable Long netId) {
        try {
            List<TNetworkHost> byNetId = networkHostRepository.findByNetId(netId);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, byNetId));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }


    /**
     * 删除文件
     * 1.需要先检查是不是有关联的部署单元
     *
     * @param id
     * @return
     */
    @PostMapping("/deploy-network/del/{id}")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity delNetwork(@PathVariable Long id) {
        try {
            List<TNetworkHost> byNetId = networkHostRepository.findByNetId(id);
            if (!isEmpty(byNetId)) {
                throw new Exception("还存在关联的节点,请先解除关联");
            }
            TNetwork net = networkRepository.findOne(id);
            if (isEmpty(net)) {
                throw new Exception("数据不存在不存在");
            }
            net.setDeleted(DELETED_STATUS);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "删除网络成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/deploy-network/sync-network/{id}")
    public ResponseEntity syncNetwork(@PathVariable Long id) {
        try {
            TNetwork network = networkRepository.findOne(id);
            List<TNetworkHost> byNetId = networkHostRepository.findByNetId(id);
            List<String> hosts = new ArrayList<>();
            byNetId.forEach(x -> {
                THost hosts1 = hostRepository.findOne(x.getHostId());
                if (!isEmpty(hosts1)) {
                    hosts.add(IPUtils.transferIp(hosts1.getIp()));
                }
            });
            SyncNetworkVo vo = new SyncNetworkVo();
            vo.setHosts(hosts);
            vo.setDriver(network.getDriver());
            vo.setNetworkName(network.getNetworkName());
            vo.setOpt(network.getOpt());
            vo.setSubnet(network.getSubnet());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, vo));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }
}
