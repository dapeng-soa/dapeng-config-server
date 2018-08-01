package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.THost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface HostRepository extends JpaRepository<THost,Long> {

    /**
     * 通过setId查找节点
     * @param setId
     * @return
     */
    List<THost> findBySetId(long setId);

    List<THost> findTop1ByIdOrderByUpdatedAtDesc(Long id);
}
