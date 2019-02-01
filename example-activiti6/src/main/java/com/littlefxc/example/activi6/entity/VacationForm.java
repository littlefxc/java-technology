package com.littlefxc.example.activi6.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 请假单信息表
 */
@Data
@Entity
@Table(name = "vacation_form")
public class VacationForm implements Serializable {
    private static final long serialVersionUID = -9086251673775110627L;
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    /**
     * 申请者
     */
    private String applicant;

    /**
     * 审批者
     */
    private String approver;

    /**
     * 申请所处状态
     */
    @Transient
    private String state;
}