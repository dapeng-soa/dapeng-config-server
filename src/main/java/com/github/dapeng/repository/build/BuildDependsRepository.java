package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TBuildDepends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface BuildDependsRepository extends JpaRepository<TBuildDepends, Long>, JpaSpecificationExecutor<TBuildDepends> {
    /**
     * 根据任务id查找构建依赖
     *
     * @param id
     * @return
     */
    List<TBuildDepends> findByTaskId(Long id);
}
