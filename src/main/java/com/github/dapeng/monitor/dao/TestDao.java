package com.github.dapeng.monitor.dao;


import com.github.dapeng.entity.config.ApiKeyInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import scala.collection.immutable.List;

/**
 * @author huyj
 * @Created 2018-08-01 18:39
 */
public interface TestDao extends PagingAndSortingRepository<ApiKeyInfo, Long>, JpaSpecificationExecutor<ApiKeyInfo> {


    @Query("from ApiKeyInfo t where id = :id")
    List<ApiKeyInfo> queryFamilyList(@Param("id") Long id);

}
