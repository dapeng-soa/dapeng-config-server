package com.github.dapeng.entity.deploy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_files_unit")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TFilesUnit {

    @Id
    private long id;
    @Column(name = "file_id")
    private long fileId;
    @Column(name = "unit_id")
    private long unitId;
    @Column(name = "create_at")
    private java.sql.Timestamp createAt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }


    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }


    public java.sql.Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(java.sql.Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "TFilesUnit{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", unitId=" + unitId +
                ", createAt=" + createAt +
                '}';
    }
}
