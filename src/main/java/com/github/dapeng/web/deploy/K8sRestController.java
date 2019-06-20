package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.k8s.yaml.YamlParseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.dapeng.common.Commons.LOADED_DATA;
import static com.github.dapeng.common.Commons.SUCCESS_CODE;

/**
 * @author huyj
 * @Created 2019-06-18 09:22
 */
@RestController
@RequestMapping("/api")
//@Transactional(rollbackFor = Exception.class)docker
public class K8sRestController {

    @PostMapping("/k8s/yaml-convert")
    public ResponseEntity<?> convertK8sYaml(@RequestBody String yamlContext) {
        System.out.println("******************************************接收到xml参数：\n"+yamlContext);
        return ResponseEntity.ok(Resp.of(SUCCESS_CODE, LOADED_DATA, YamlParseUtils.buildK8sYamlByContext(yamlContext)));
    }
}
