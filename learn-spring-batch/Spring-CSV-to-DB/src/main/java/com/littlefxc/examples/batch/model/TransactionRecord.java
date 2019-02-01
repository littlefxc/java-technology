package com.littlefxc.examples.batch.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author fengxuechao
 */
@Data
@XmlRootElement(name = "transactionRecord")
public class TransactionRecord {

    private String username;

    private int userId;

    private Date transactionDate;

    private double amount;
}