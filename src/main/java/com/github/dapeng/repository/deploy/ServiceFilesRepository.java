package com.github.dapeng.repository.deploy;

import com.github.dapeng.entity.deploy.TServiceFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/27 11:30
 * email :yq1724555319@gmail.com
 */

public interface ServiceFilesRepository extends JpaRepository<TServiceFiles, Long>, JpaSpecificationExecutor<TServiceFiles> {
    /**
     * 按照内外文件名查找
     * @param fileName
     * @param fileExtname
     * @return
     */
    List<TServiceFiles> findByFileNameAndFileExtName(String fileName, String fileExtname);
}
