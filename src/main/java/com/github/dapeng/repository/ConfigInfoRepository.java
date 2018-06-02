package com.github.dapeng.repository;

import com.github.dapeng.entity.ConfigInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with struy.
 * Create by 2018/5/31 11:59
 * email :yq1724555319@gmail.com
 */

@Repository
public interface ConfigInfoRepository extends JpaRepository<ConfigInfo,Long> {

    /**
     * 模糊查找配置，方法名，版本号
     * @param serviceName
     * @param version
     * @param pageable
     * @return
     */
    Page<ConfigInfo> findAllByServiceNameLikeOrVersionLike(String serviceName, String version, Pageable pageable);
}
