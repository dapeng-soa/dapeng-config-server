package com.github.dapeng.repository;

import com.github.dapeng.dto.ConfigInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with struy.
 * Create by 2018/5/31 11:59
 * email :yq1724555319@gmail.com
 */

@Repository
public interface ConfigInfoRepository extends CrudRepository<ConfigInfo,Long> {

}
