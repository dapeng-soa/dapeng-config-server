package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/27 11:30
 * email :yq1724555319@gmail.com
 */

public interface NetworkRepository extends JpaRepository<TNetwork, Long>, JpaSpecificationExecutor<TNetwork> {

    /**
     * 根据网络名称查找
     * @param name
     * @return
     */
    List<TNetwork> findByNetworkName(String name);

}
