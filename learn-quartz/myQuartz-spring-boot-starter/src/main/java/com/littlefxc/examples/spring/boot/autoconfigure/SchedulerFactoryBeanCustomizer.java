package com.littlefxc.examples.spring.boot.autoconfigure;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 回调接口，可以由希望在完全初始化之前自定义Quartz SchedulerFactoryBean的bean实现，特别是调整其配置。
 * @author fengxuechao
 */
@FunctionalInterface
public interface SchedulerFactoryBeanCustomizer {

	/**
	 * 自定义{@link SchedulerFactoryBean}.
	 * @param schedulerFactoryBean the scheduler to customize
	 */
	void customize(SchedulerFactoryBean schedulerFactoryBean);

}
