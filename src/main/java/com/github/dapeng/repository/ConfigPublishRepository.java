package com.github.dapeng.repository;

import com.github.dapeng.entity.config.ConfigPublishHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/11 18:02
 * email :yq1724555319@gmail.com
 */
@Repository
public interface ConfigPublishRepository extends JpaRepository<ConfigPublishHistory, Long> {


    /**
     * 根据服务名查找发布历史/分页
     *
     * @param serviceName
     * @param pageable
     * @return
     */
    Page<ConfigPublishHistory> findAllByServiceName(String serviceName, Pageable pageable);

    /**
     * 查找全量的发布历史
     *
     * @param serviceName
     * @return
     */
    List<ConfigPublishHistory> findAllByServiceName(String serviceName);
}
