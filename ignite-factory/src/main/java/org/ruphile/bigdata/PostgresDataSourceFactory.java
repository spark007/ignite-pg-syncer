package org.ruphile.bigdata;

import org.postgresql.ds.PGSimpleDataSource;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.io.Serializable;

// 必须实现 Serializable 接口，以便 Ignite 可以将其发送给其他节点
public class PostgresDataSourceFactory implements Factory<DataSource>, Serializable {
    // 尽管 PGSimpleDataSource 本身可序列化，但最佳实践是将配置参数作为字段
    // 这里为了简洁，直接在 create() 方法中硬编码配置

    @Override
    public DataSource create() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{"localhost"});
        ds.setPortNumbers(new int[]{5432});
        ds.setUser("root");
        ds.setPassword("123456");
        ds.setDatabaseName("rocket");
        return ds;
    }

    public static void main(String[] args) {
        PostgresDataSourceFactory factory = new PostgresDataSourceFactory();
        DataSource ds = factory.create();
    }
}
