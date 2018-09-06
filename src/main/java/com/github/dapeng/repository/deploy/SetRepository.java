package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface SetRepository extends JpaRepository<TSet, Long>, JpaSpecificationExecutor<TSet> {

    /**
     * 获取
     *
     * @param id
     * @return
     */
    List<TSet> findTop1ByIdOrderByUpdatedAtDesc(Long id);

    /**
     * 根据名字查找
     *
     * @param name
     * @return
     */
    List<TSet> findByName(String name);
}
