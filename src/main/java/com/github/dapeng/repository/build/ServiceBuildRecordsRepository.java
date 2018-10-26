package com.github.dapeng.repository.build;

import com.github.dapeng.entity.build.TServiceBuildRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author with struy.
 * Create by 2018/9/19 17:46
 * email :yq1724555319@gmail.com
 */

public interface ServiceBuildRecordsRepository extends JpaRepository<TServiceBuildRecords, Long>, JpaSpecificationExecutor<TServiceBuildRecords> {
}
