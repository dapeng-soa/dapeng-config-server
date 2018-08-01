package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 18:57
 * email :yq1724555319@gmail.com
 */

public interface SetRepository  extends JpaRepository<TSet,Long> {

    /**
     * 获取
     * @param id
     * @return
     */
    List<TSet> findTop1ByIdOrderByUpdatedAtDesc(Long id);
}
