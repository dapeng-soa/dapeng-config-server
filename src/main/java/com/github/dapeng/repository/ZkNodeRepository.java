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

}
