package com.github.dapeng.repository.system;

import com.github.dapeng.entity.system.TRoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/14 14:36
 * email :yq1724555319@gmail.com
 */

public interface RoleUserRepository extends JpaRepository<TRoleUser, Long> {

    /**
     * 根据用户id查找关联
     *
     * @param userId
     * @return
     */
    List<TRoleUser> findAllByUserId(Long userId);


    /**
     * 根据用户id和角色id查找关联
     *
     * @param userId
     * @param roleId
     * @return
     */
    List<TRoleUser> findByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 删除关联
     *
     * @param userId
     * @param roleId
     */
    void deleteByUserIdAndRoleId(Long userId, Long roleId);

}
