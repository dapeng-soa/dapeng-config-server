package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TDeployUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface DeployUnitRepository extends JpaRepository<TDeployUnit, Long>, JpaSpecificationExecutor<TDeployUnit> {

    List<TDeployUnit> findAllBySetId(long setId);

    List<TDeployUnit> findAllBySetIdAndServiceId(long setId, long serviceId);

    List<TDeployUnit> findAllBySetIdAndHostId(long setId, long hostId);

    List<TDeployUnit> findAllBySetIdAndHostIdAndIdIsNot(long setId, long hostId,long id);

    boolean existsAllByHostId(long hostId);

    boolean existsAllByServiceId(long serviceId);

    List<TDeployUnit> findTop1ByIdOrderByUpdatedAtDesc(Long id);
}
