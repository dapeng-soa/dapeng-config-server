package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TOperationJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface DeployOpJournalRepository extends JpaRepository<TOperationJournal, Long>,JpaSpecificationExecutor<TOperationJournal> {
}
