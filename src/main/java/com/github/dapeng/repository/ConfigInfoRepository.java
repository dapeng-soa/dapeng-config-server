package com.github.dapeng.repository;

import com.github.dapeng.entity.ConfigInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/5/31 11:59
 * email :yq1724555319@gmail.com
 */

@Repository
public interface ConfigInfoRepository extends JpaRepository<ConfigInfo, Long>, JpaSpecificationExecutor<ConfigInfo> {

    /**
     * 模糊查找配置，方法名，版本号
     *
     * @param serviceName
     * @param status      状态
     * @param pageable
     * @return
     */
    Page<ConfigInfo> findAllByStatusIsNotAndServiceNameLike(int status, String serviceName, Pageable pageable);

    /**
     * 通过配置名称查找
     *
     * @return
     */
    List<ConfigInfo> findByServiceName(String serviceName);

    /**
     * 检查指定服务配置是否已经存在
     *
     * @param serviceName
     * @param notIs       排除某个状态
     * @return
     */
    boolean existsConfigInfoByServiceNameAndStatusIsNot(String serviceName, int notIs);
}
