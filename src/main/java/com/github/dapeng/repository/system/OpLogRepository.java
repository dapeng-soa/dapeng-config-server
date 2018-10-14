package com.github.dapeng.repository.system;

import com.github.dapeng.entity.system.TOpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author with struy.
 * Create by 2018/9/14 14:36
 * email :yq1724555319@gmail.com
 */

public interface OpLogRepository extends JpaRepository<TOpLog, Long>,JpaSpecificationExecutor<TOpLog> {

}
