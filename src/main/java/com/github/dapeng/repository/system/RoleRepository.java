package com.github.dapeng.repository.system;

import com.github.dapeng.entity.system.TRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/14 14:36
 * email :yq1724555319@gmail.com
 */

public interface RoleRepository extends JpaRepository<TRole, Long>, JpaSpecificationExecutor<TRole> {
    /**
     *
     * @param role
     * @return
     */
    List<TRole> findAllByRoleIsNot(String role);
}
