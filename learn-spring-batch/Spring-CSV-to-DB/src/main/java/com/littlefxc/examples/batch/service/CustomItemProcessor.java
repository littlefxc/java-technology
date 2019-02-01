package com.littlefxc.examples.batch.service;

import com.littlefxc.examples.batch.model.TransactionRecord;
import org.springframework.batch.item.ItemProcessor;

/**
 * 处理输入资源对象
 *
 * @author fengxuechao
 * @date 2019/1/4
 **/
//@Component
public class CustomItemProcessor implements ItemProcessor<TransactionRecord, TransactionRecord> {

    public TransactionRecord process(TransactionRecord item) throws Exception {
        return item;
    }
}
