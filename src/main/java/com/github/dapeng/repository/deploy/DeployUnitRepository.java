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

    List<TDeployUnit> findAllBySetIdOrderByUpdatedAtDesc(long setId);

    List<TDeployUnit> findAllBySetIdAndServiceIdOrderByUpdatedAtDesc(long setId, long serviceId);

    List<TDeployUnit> findAllBySetIdAndHostId(long setId, long hostId);

    List<TDeployUnit> findAllBySetIdAndHostIdAndIdIsNot(long setId, long hostId, long id);

    List<TDeployUnit> findAllBySetId(long setId);

    boolean existsAllByHostId(long hostId);

    boolean existsAllByServiceId(long serviceId);

    List<TDeployUnit> findByOrderByUpdatedAtDesc();

    List<TDeployUnit> findTop1ByIdOrderByUpdatedAtDesc(Long id);
}
