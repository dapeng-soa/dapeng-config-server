package com.github.dapeng.repository;

import com.github.dapeng.entity.config.ApiKeyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with struy.
 * Create by 2018/6/13 00:21
 * email :yq1724555319@gmail.com
 */
@Repository
public interface ApiKeyInfoRepository extends JpaRepository<ApiKeyInfo, Long> {

}
