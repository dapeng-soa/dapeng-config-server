package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TNetworkHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/27 11:30
 * email :yq1724555319@gmail.com
 */

public interface NetworkHostRepository extends JpaRepository<TNetworkHost, Long>, JpaSpecificationExecutor<TNetworkHost> {

    /**
     * 通过netId查找关联
     * @param netId
     * @return
     */
    List<TNetworkHost> findByNetId(Long netId);

    /**
     * 通过netid和节点id查找
     * @param netId
     * @param hostId
     * @return
     */
    List<TNetworkHost> findByNetIdAndHostId(Long netId,Long hostId);

    /**
     * 通过hostId查找
     * @param hostId
     * @return
     */
    List<TNetworkHost> findByHostId(Long hostId);

    /**
     * 删除
     * @param netId
     * @param hostId
     */
    void deleteByNetIdAndHostId(Long netId,Long hostId);

}
