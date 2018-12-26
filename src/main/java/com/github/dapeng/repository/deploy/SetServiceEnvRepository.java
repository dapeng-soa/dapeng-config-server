package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TSetServiceEnv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/5 10:36
 * email :yq1724555319@gmail.com
 */

public interface SetServiceEnvRepository extends JpaRepository<TSetServiceEnv, Long> {
    /**
     * 某个环境集下的子环境变量
     *
     * @param setId
     * @return
     */
    List<TSetServiceEnv> findAllBySetId(Long setId);

    /**
     * 查看是否已经存在配置
     * @param setId
     * @param serviceId
     * @return
     */
    Boolean existsBySetIdAndServiceId(Long setId, Long serviceId);

    /**
     * 按照服务查找最新一条记录
     *
     * @param serviceId
     * @return
     */
    List<TSetServiceEnv> findTop1BySetIdAndServiceIdOrderByUpdatedAtDesc(Long setId, Long serviceId);
}
