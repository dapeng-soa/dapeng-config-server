package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.THost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface HostRepository extends JpaRepository<THost, Long>, JpaSpecificationExecutor<THost> {

    /**
     * 通过setId查找节点
     *
     * @param setId
     * @return
     */
    List<THost> findBySetId(long setId);

    /**
     * 根据主机id查询最新一条记录
     *
     * @param id
     * @return
     */
    List<THost> findTop1ByIdOrderByUpdatedAtDesc(Long id);

    /**
     * 查询最新一条记录
     *
     * @return
     */
    List<THost> findTop1ByOrderByUpdatedAtDesc();

    /**
     * 根据名称查查找
     *
     * @param name
     * @return
     */
    List<THost> findByName(String name);
}
