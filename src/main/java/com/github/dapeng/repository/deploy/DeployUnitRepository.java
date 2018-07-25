package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TDeployUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface DeployUnitRepository extends JpaRepository<TDeployUnit,Long> {

    List<TDeployUnit> findAllBySetId(long setId);
}
