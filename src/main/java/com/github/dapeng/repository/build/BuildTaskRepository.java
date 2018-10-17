package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TBuildTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface BuildTaskRepository extends JpaRepository<TBuildTask, Long>, JpaSpecificationExecutor<TBuildTask> {
    /**
     * 跟据hostId查找构建任务
     *
     * @param hostId
     * @return
     */
    List<TBuildTask> findByHostId(Long hostId);
}
