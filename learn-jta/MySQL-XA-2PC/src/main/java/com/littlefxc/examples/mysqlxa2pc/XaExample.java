package com.littlefxc.examples.mysqlxa2pc;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author fengxuechao
 */
public class XaExample {

    public static void main(String[] args) throws SQLException {
        String url1 = "jdbc:mysql://192.168.120.63:3306/learn?useSSL=false&serverTimezone=UTC";
        String url2 = "jdbc:mysql://192.168.120.63:3306/learn2?useSSL=false&serverTimezone=UTC";
        String url3 = "jdbc:mysql://localhost:3306/learn?useSSL=false&serverTimezone=UTC";

        // 从不同的数据库获取数据库数据源
        MysqlXADataSource ds1 = getDataSource(url1, "root", "123456");
        MysqlXADataSource ds2 = getDataSource(url2, "root", "123456");

        // 数据库1获取连接
        XAConnection xAConn1 = ds1.getXAConnection();
        Connection conn1 = xAConn1.getConnection();
        XAResource xaResource1 = xAConn1.getXAResource();
        Statement statement1 = conn1.createStatement();

        // 数据库2获取连接
        XAConnection xAConn2 = ds2.getXAConnection();
        Connection conn2 = xAConn2.getConnection();
        XAResource xaResource2 = xAConn2.getXAResource();
        Statement statement2 = conn2.createStatement();

        Xid xid1 = new MysqlXid(new byte[]{0x01}, new byte[]{0x02}, 100);
        Xid xid2 = new MysqlXid(new byte[]{0x011}, new byte[]{0x012}, 100);

        try {
            // 事务分支1关联事务sql语句
            xaResource1.start(xid1, XAResource.TMNOFLAGS);
            int update1 = statement1.executeUpdate("update account_from set money = money - 0.5 where id = 1");
            xaResource1.end(xid1, XAResource.TMSUCCESS);

            // 事务分支2关联事务sql语句
            xaResource2.start(xid2, XAResource.TMNOFLAGS);
            int update2 = statement2.executeUpdate("update account_to set money = money + 0.5 where id = 1");
            xaResource2.end(xid2, XAResource.TMSUCCESS);

            // 两阶段提交协议第一阶段
            int ret1 = xaResource1.prepare(xid1);
            int ret2 = xaResource2.prepare(xid2);

            // 两阶段提交协议第二阶段
            if (XAResource.XA_OK == ret1 && XAResource.XA_OK == ret2) {
                xaResource1.commit(xid1, false);
                xaResource2.commit(xid2, false);

                System.out.println("result:" + update1 + ", result2:" + update2);
            }
        } catch (XAException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 MysqlXADataSource
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static MysqlXADataSource getDataSource(String url, String username, String password) {
        MysqlXADataSource dataSource = new MysqlXADataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
