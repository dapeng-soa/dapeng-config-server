package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TServiceBuildRecords;
import com.github.dapeng.entity.build.TServiceBuildRecordsVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface ServiceBuildRecordsVoRepository extends JpaRepository<TServiceBuildRecordsVo, Long>, JpaSpecificationExecutor<TServiceBuildRecordsVo> {

    /**
     * 根据host查找构建列表
     *
     * @param host
     * @return
     */
    List<TServiceBuildRecordsVo> findByAgentHostOrderByCreatedAtDesc(String host);

    /**
     * 根据任务id查找构建列表
     * @param taskId
     * @return
     */
    List<TServiceBuildRecordsVo> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    List<TServiceBuildRecordsVo> findByOrderByCreatedAtDesc();

    List<TServiceBuildRecordsVo> findByBuildServiceAndAgentHostOrderByCreatedAtDesc(String service, String host);
}
