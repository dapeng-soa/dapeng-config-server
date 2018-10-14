package com.github.dapeng.web.system;

import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.system.TOpLog;
import com.github.dapeng.repository.system.OpLogRepository;
import com.github.dapeng.vo.SystemLogOps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.dapeng.common.Commons.LOADED_DATA;
import static com.github.dapeng.common.Commons.SUCCESS_CODE;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/10/10 13:10
 * email :yq1724555319@gmail.com
 */

@RestController
@RequestMapping("/api")
public class OpLogController {

    @Autowired
    OpLogRepository logRepository;

    /**
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @param search
     * @param opType
     * @return
     */
    @GetMapping("/system-logs")
    public ResponseEntity systemLogs(@RequestParam(required = false, defaultValue = "0") int offset,
                                     @RequestParam(required = false, defaultValue = "100000") int limit,
                                     @RequestParam(required = false) String sort,
                                     @RequestParam(required = false, defaultValue = "desc") String order,
                                     @RequestParam(required = false, defaultValue = "") String search,
                                     @RequestParam(required = false, defaultValue = "") String opType,
                                     @RequestParam(required = false, defaultValue = "") String opName,
                                     @RequestParam(required = false, defaultValue = "") String operator,
                                     @RequestParam(required = false, defaultValue = "") String resultType) {

        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "createdAt" : sort));

        Page<TOpLog> page = logRepository.findAll((root, query, cb) -> {
            Path<String> operName = root.get("operName");
            Path<String> operator1 = root.get("operator");
            Path<String> resultMsg = root.get("resultMsg");
            Path<String> operResult = root.get("operResult");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(operName, "%" + search + "%"), cb.like(operator1, "%" + search + "%"), cb.like(resultMsg, "%" + search + "%")));

            if (!isEmpty(opType)) {
                ps.add(cb.like(operName, opType + "%"));
            }
            if (!isEmpty(opName)) {
                ps.add(cb.equal(operName, opName));
            }
            if (!isEmpty(operator)) {
                ps.add(cb.equal(operator1, operator));
            }
            if (!isEmpty(resultType)) {
                ps.add(cb.equal(operResult, resultType));
            }

            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));

    }

    public String toPrettyFormat(String json) {
        List<Object> requests = new Gson().fromJson(json, new TypeToken<List<Object>>() {
        }.getType());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(requests);
    }


    /**
     * @return
     */
    @GetMapping("/system-logs/ops")
    public ResponseEntity ops() {
        List<TOpLog> logs = logRepository.findAll();
        List<String> opNames = logs.stream().map(TOpLog::getOperName)
                .distinct()
                .collect(Collectors.toList());
        List<String> perators = logs.stream().map(TOpLog::getOperator)
                .distinct()
                .collect(Collectors.toList());
        SystemLogOps logOps = new SystemLogOps();
        logOps.setOpNames(opNames);
        logOps.setPerators(perators);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, logOps));
    }

    @GetMapping("/system-log/{id}")
    public ResponseEntity getOpLogById(@PathVariable Long id) {
        TOpLog opLog = logRepository.getOne(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, opLog));
    }


}
