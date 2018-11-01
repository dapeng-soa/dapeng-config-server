package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.deploy.TOperationJournal;
import com.github.dapeng.repository.deploy.DeployOpJournalRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.vo.DeployJournalVo;
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

import static com.github.dapeng.common.Commons.LOADED_DATA;
import static com.github.dapeng.common.Commons.SUCCESS_CODE;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/8/14 11:31
 * email :yq1724555319@gmail.com
 * 部署流水
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Exception.class)
public class DeployOpJournalController {


    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DeployOpJournalRepository journalRepository;

    /**
     * @return 部署记录
     */
    @GetMapping("/deploy-journals")
    public ResponseEntity deployUnits(@RequestParam(required = false, defaultValue = "0") int offset,
                                      @RequestParam(required = false, defaultValue = "100000") int limit,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(required = false, defaultValue = "desc") String order,
                                      @RequestParam(required = false, defaultValue = "") String search,
                                      @RequestParam(required = false, defaultValue = "0") Long setId,
                                      @RequestParam(required = false, defaultValue = "0") Long hostId,
                                      @RequestParam(required = false, defaultValue = "0") Long serviceId,
                                      @RequestParam(required = false) String gitTag,
                                      @RequestParam(required = false, defaultValue = "0") Long opType) {
        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "createdAt" : sort));


        Page<TOperationJournal> page = journalRepository.findAll((root, query, cb) -> {
            Path<Long> setId1 = root.get("setId");
            Path<Long> hostId1 = root.get("hostId");
            Path<Long> serviceId1 = root.get("serviceId");
            Path<String> gitTag1 = root.get("gitTag");
            Path<String> imageTag = root.get("imageTag");
            Path<Integer> opFlag = root.get("opFlag");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(gitTag1, "%" + search + "%"), cb.like(imageTag, "%" + search + "%")));
            //这里可以设置任意条查询条件
            if (!isEmpty(setId)) {
                ps.add(cb.equal(setId1, setId));
            }
            if (!isEmpty(hostId)) {
                ps.add(cb.equal(hostId1, hostId));
            }
            if (!isEmpty(serviceId)) {
                ps.add(cb.equal(serviceId1, serviceId));
            }
            if (!isEmpty(opType)) {
                ps.add(cb.equal(opFlag, opType));
            }
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);

        Page<DeployJournalVo> voPage = page.map(u -> toVo(u));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, voPage));
    }

    private DeployJournalVo toVo(TOperationJournal u) {
        DeployJournalVo vo = new DeployJournalVo();
        vo.setId(u.getId());
        vo.setSetId(u.getSetId());
        vo.setSetName(setRepository.getOne(u.getSetId()).getName());
        vo.setHostId(u.getHostId());
        vo.setHostName(hostRepository.getOne(u.getHostId()).getName());
        vo.setServiceId(u.getServiceId());
        vo.setServiceName(serviceRepository.getOne(u.getServiceId()).getName());
        vo.setCreatedBy(u.getCreatedBy());
        vo.setCreatedAt(u.getCreatedAt());
        vo.setGitTag(u.getGitTag());
        vo.setImageTag(u.getImageTag());
        vo.setOpFlag(u.getOpFlag());
        vo.setDiff(u.getDiff());
        vo.setYml(u.getYml());
        return vo;
    }


    /**
     * 根据Id获取当前流水信息
     *
     * @return
     */
    @GetMapping("/deploy-journal/{id}")
    public ResponseEntity<?> deployUnitById(@PathVariable long id) {
        TOperationJournal journal = journalRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, toVo(journal)));
    }


}
