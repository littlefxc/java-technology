package com.fengxuechao.designpattern.proxy.statics;

/**
 * 出自《设计模式之禅》第12章 代理模式
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/28
 */
public interface IGamePlayer {

    void login(String user, String password);

    void killBoss();

    void upgrade();
}
