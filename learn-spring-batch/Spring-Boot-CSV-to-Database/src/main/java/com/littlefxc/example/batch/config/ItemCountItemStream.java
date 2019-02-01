package com.littlefxc.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

/**
 * 实现计数定期处理的记录的数量
 */
@Slf4j
public class ItemCountItemStream implements ItemStream {

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("ItemCount: " + executionContext.get("personItemReader.read.count"));
    }

    @Override
    public void close() throws ItemStreamException {
    }
}