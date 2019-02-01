package com.littlefxc.example.suitcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class) // 1. 更改测试运行方式为 Suite
// 2. 将测试类传入进来
@Suite.SuiteClasses({TaskTwoTest.class, TaskOneTest.class, TaskThreeTest.class})
public class SuitTest {
    /**
     * 测试套件的入口类只是组织测试类一起进行测试，无任何测试方法，
     */
}
