package com.fengxuechao.designpattern.proxy.statics.game;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/28
 */
public class GamePlayerProxy implements IGamePlayer {

    private IGamePlayer gamePlayer;

    /**
     * 通过构造函数传递要对谁进行代练
     *
     * @param gamePlayer
     */
    public GamePlayerProxy(IGamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    /**
     * 代练登录
     *
     * @param user
     * @param password
     */
    public void login(String user, String password) {
        this.gamePlayer.login(user, password);
    }

    /**
     * 代练杀怪
     */
    public void killBoss() {
        this.gamePlayer.killBoss();
    }

    /**
     * 代练升级
     */
    public void upgrade() {
        this.gamePlayer.upgrade();
    }
}
