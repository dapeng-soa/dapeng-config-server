package com.github.dapeng.controller;

import com.github.dapeng.dto.ConfigInfo;
import com.github.dapeng.repository.ConfigInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author with struy.
 * Create by 2018/5/31 00:17
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
public class ConfigRestController {

    @Autowired
    ConfigInfoRepository repository;

    /**
     * 添加配置
     */

    @PostMapping(value = "/config/add")
    public ResponseEntity<?> addConfig(ConfigInfo configInfo) {
        repository.save(configInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("添加配置成功");
    }

    @PostMapping(value = "config/delete/{id}")
    public ResponseEntity<?> addConfig(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("删除配置成功");
    }

    @GetMapping(value = "config")
    public ResponseEntity<?> getConfig(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(repository.findAll());
    }


}
