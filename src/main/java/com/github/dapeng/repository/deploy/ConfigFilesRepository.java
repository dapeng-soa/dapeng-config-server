package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TConfigFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/14 14:36
 * email :yq1724555319@gmail.com
 */

public interface ConfigFilesRepository extends JpaRepository<TConfigFiles, Long> {

    /**
     * 根据环境集查找
     * @param setId
     * @return
     */
    List<TConfigFiles> findBySetId(Long setId);
}
