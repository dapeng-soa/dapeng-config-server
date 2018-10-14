package com.github.dapeng.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author with struy.
 * Create by 2018/10/9 21:39
 * email :yq1724555319@gmail.com
 */
@Entity
@Table(name = "t_op_log")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TOpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 操作名称，方法名
     */
    @Column(name = "oper_name")
    private String operName;

    /**
     * 操作人
     */
    @Column(name = "operator")
    private String operator;


    /**
     * 操作参数
     */
    @Column(name = "oper_params")
    private String operParams;

    /**
     * 操作结果 状态码
     */
    @Column(name = "oper_result")
    private String operResult;

    /**
     * 结果消息
     */
    @Column(name = "result_msg")
    private String resultMsg;

    /**
     * 操作时间
     */
    @Column(name = "oper_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Timestamp operTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperParams() {
        return operParams;
    }

    public void setOperParams(String operParams) {
        this.operParams = operParams;
    }

    public String getOperResult() {
        return operResult;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Timestamp getOperTime() {
        return operTime;
    }

    public void setOperTime(Timestamp operTime) {
        this.operTime = operTime;
    }

    @Override
    public String toString() {
        return "TOpLog{" +
                "id=" + id +
                ", operName='" + operName + '\'' +
                ", operator='" + operator + '\'' +
                ", operParams='" + operParams + '\'' +
                ", operResult='" + operResult + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", operTime=" + operTime +
                '}';
    }
}
