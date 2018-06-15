package com.github.dapeng.repository;

import com.github.dapeng.entity.ZkNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huyj
 * @Created 2018/6/14 16:01
 */
@Repository
public interface ZkNodeRepository extends JpaRepository<ZkNode, Long>, JpaSpecificationExecutor<ZkNode> {
    /**
     * 通过配置名称查找
     *
     * @return
     */
    ZkNode findById(long id);

    /**
     * 通过配置名称查找
     *
     * @return
     */
    List<ZkNode> findAll();
}
