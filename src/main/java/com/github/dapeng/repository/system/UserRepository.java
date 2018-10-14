package com.github.dapeng.repository.system;

import com.github.dapeng.entity.system.TUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author with struy.
 * Create by 2018/9/14 14:36
 * email :yq1724555319@gmail.com
 */

public interface UserRepository extends JpaRepository<TUsers, Long>, JpaSpecificationExecutor<TUsers> {

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    TUsers findByUsername(String username);
}
