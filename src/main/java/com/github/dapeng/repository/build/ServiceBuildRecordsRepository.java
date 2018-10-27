package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TServiceBuildRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface ServiceBuildRecordsRepository extends JpaRepository<TServiceBuildRecords, Long>, JpaSpecificationExecutor<TServiceBuildRecords> {
    /**
     *  查找一台主机上是否存在未完成的构建
     * @param host
     * @param status
     * @return
     */
    List<TServiceBuildRecords> findByAgentHostAndStatusIn(String host,List<Long> status);

    /**
     * 查找构建列表
     * @param host
     * @return
     */
    List<TServiceBuildRecords> findByAgentHostOrderByCreatedAtDesc(String host);
}
