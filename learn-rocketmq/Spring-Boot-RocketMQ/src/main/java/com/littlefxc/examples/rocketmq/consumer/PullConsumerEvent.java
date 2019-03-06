package com.littlefxc.examples.rocketmq.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PullConsumerEvent {

    private static final long serialVersionUID = -8038481034359034319L;

    private String name;

    public PullConsumerEvent(String name) {
        this.name = name;
    }
}
