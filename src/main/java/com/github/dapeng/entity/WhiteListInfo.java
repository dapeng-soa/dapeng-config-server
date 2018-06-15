package com.github.dapeng.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author struy
 */
@Entity
@Table(name = "White_list_info")
public class WhiteListInfo {

    @Id
    private long id;
    @Column(name = "service")
    private String serveice;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getServeice() {
        return serveice;
    }

    public void setServeice(String serveice) {
        this.serveice = serveice;
    }

}
