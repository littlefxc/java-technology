package com.littlefxc.examples.spring.boot.autoconfigure;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 模仿了：{@link org.springframework.boot.autoconfigure.quartz.AutowireCapableBeanJobFactory}
 *
 * @author fengxuechao
 * @date 12/19/2018
 * @see <a href="http://blog.btmatthews.com/?p=40#comment-33797">注入Spring上下文(applicationContext)
 */
public class AutoSchedulerJobFactory extends SpringBeanJobFactory {

    private AutowireCapableBeanFactory beanFactory;

    AutoSchedulerJobFactory(AutowireCapableBeanFactory factory) {
        beanFactory = factory;
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        this.beanFactory.initializeBean(job, null);
        return job;
    }
}
