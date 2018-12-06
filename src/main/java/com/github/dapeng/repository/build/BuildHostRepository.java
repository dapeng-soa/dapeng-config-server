package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TBuildHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface BuildHostRepository extends JpaRepository<TBuildHost,Long>,JpaSpecificationExecutor<TBuildHost> {
    /**
     *
     * @return
     */
    List<TBuildHost> findAllByOrderByName();
}
