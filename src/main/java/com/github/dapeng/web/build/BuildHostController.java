package com.github.dapeng.web.build;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.dto.BuildHostDto;
import com.github.dapeng.entity.build.TBuildHost;
import com.github.dapeng.repository.build.BuildHostRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.vo.BuildHostVo;
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
import java.util.List;

import static com.github.dapeng.common.Commons.*;

/**
 * @author with struy.
 * Create by 2018/9/19 17:47
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class BuildHostController {
    @Autowired
    BuildHostRepository buildHostRepository;

    @GetMapping("/build-hosts")
    public ResponseEntity buildHosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                     @RequestParam(required = false, defaultValue = "100000") int limit,
                                     @RequestParam(required = false) String sort,
                                     @RequestParam(required = false, defaultValue = "desc") String order,
                                     @RequestParam(required = false, defaultValue = "") String search) {
        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TBuildHost> page = buildHostRepository.findAll((root, query, cb) -> {
            Path<String> name = root.get("name");
            Path<String> remark = root.get("remark");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(name, "%" + search + "%"), cb.like(remark, "%" + search + "%")));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        Page<BuildHostVo> vos = page.map(x -> toVo(x));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, vos));
    }

    @GetMapping("/build-host/{id}")
    public ResponseEntity getBuildHost(@PathVariable Long id) {
        TBuildHost host = buildHostRepository.getOne(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, toVo(host)));
    }

    private BuildHostVo toVo(TBuildHost host) {
        BuildHostVo vo = new BuildHostVo();
        vo.setId(host.getId());
        vo.setHost(IPUtils.transferIp(host.getHost()));
        vo.setName(host.getName());
        vo.setRemark(host.getRemark());
        vo.setCreatedAt(host.getCreatedAt());
        vo.setUpdatedAt(host.getUpdatedAt());
        return vo;
    }

    @PostMapping("/build-host/add")
    public ResponseEntity addBuildHost(@RequestBody BuildHostDto dto) {
        TBuildHost host = new TBuildHost();
        host.setCreatedAt(DateUtil.now());
        host.setUpdatedAt(DateUtil.now());
        host.setName(dto.getName());
        host.setHost(IPUtils.transferIp(dto.getHost()));
        host.setRemark(dto.getRemark());
        buildHostRepository.save(host);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
    }

    @PostMapping("/build-host/edit/{id}")
    public ResponseEntity editBuildHost(@PathVariable Long id, @RequestBody BuildHostDto dto) {
        TBuildHost buildHost = buildHostRepository.getOne(id);
        buildHost.setRemark(dto.getRemark());
        buildHost.setName(dto.getName());
        buildHost.setUpdatedAt(DateUtil.now());
        buildHost.setHost(IPUtils.transferIp(dto.getHost()));
        buildHostRepository.save(buildHost);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
    }
}
