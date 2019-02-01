package com.littlefxc.examples.batch.dao;

import com.littlefxc.examples.batch.model.TransactionRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fengxuechao
 * @date 2019/1/31
 **/
@Mapper
public interface TransactionRecordDao {

    @Insert("insert transaction_record(username, user_id, transaction_date, amount) values (#{username}, #{userId}, #{transactionDate}, #{amount})")
    int insertTransactionRecord(TransactionRecord transactionRecord);
}
