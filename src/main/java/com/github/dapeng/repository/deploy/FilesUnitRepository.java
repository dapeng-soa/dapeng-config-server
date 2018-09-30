package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TFilesUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/27 10:06
 * email :yq1724555319@gmail.com
 */

public interface FilesUnitRepository extends JpaRepository<TFilesUnit, Long>, JpaSpecificationExecutor {

    /**
     * 通过文件id查找
     *
     * @param fid
     * @return
     */
    List<TFilesUnit> findByFileId(Long fid);

    /**
     * 有没有已经绑定
     *
     * @param fid
     * @param uid
     * @return
     */
    List<TFilesUnit> findByFileIdAndUnitId(Long fid, Long uid);

    /**
     * 删除绑定关系
     *
     * @param fid
     * @param uid
     */
    void deleteByFileIdAndUnitId(Long fid, Long uid);

    /**
     * 通过部署单元获取关联的文件列表
     *
     * @param uid
     * @return
     */
    List<TFilesUnit> findByUnitId(Long uid);
}
