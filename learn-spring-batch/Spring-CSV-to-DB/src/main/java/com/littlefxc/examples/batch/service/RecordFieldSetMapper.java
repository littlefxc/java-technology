package com.littlefxc.examples.batch.service;

import com.littlefxc.examples.batch.model.TransactionRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 将读取到的数据集合转换为对象
 * @author fengxuechao
 * @date 2019/1/4
 **/
@Component
public class RecordFieldSetMapper implements FieldSetMapper<TransactionRecord> {

    public TransactionRecord mapFieldSet(FieldSet fieldSet) throws BindException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        TransactionRecord transactionRecord = new TransactionRecord();

        transactionRecord.setUsername(fieldSet.readString("username"));
        transactionRecord.setUserId(fieldSet.readInt("user_id"));
        transactionRecord.setAmount(fieldSet.readDouble("transaction_amount"));
        String dateString = fieldSet.readString("transaction_date");
        try {
            transactionRecord.setTransactionDate(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return transactionRecord;
    }
}
