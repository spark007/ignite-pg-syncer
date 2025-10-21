package org.ruphile.bigdata.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.processors.cache.store.GridCacheWriteBehindStore;
import org.ruphile.bigdata.PostgresDataSourceFactory;
import org.ruphile.bigdata.entity.RiskLog;
import org.ruphile.bigdata.entity.RiskLogKey;
import org.ruphile.bigdata.util.CacheMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;

/*
CREATE TABLE t_risk (
    sno VARCHAR(32) NOT NULL,
    uno VARCHAR(32) NOT NULL,
    business_date VARCHAR(32),
    business_time VARCHAR(32),
    object_id BIGINT,
    object_name VARCHAR(64),
    risk_name VARCHAR(64),
    risk_type VARCHAR(32),
    PRIMARY KEY (sno, uno)
);
 */
@Service
public class RiskService {
    @Resource
    private Ignite ignite;

    private IgniteCache<RiskLogKey, RiskLog> riskCache;

    @PostConstruct
    public void init() {
        String cacheName = RiskLog.class.getSimpleName().toUpperCase();
        if (ignite.cacheNames().contains(cacheName)) {
            riskCache = ignite.cache(cacheName);
            return;
        }

        CacheConfiguration<RiskLogKey, RiskLog> cacheCfg = new CacheConfiguration<>();
        cacheCfg.setName(cacheName);
        cacheCfg.setSqlSchema("BASE");
        QueryEntity queryEntity = CacheMapper.createTableEntity(RiskLogKey.class, RiskLog.class);
        cacheCfg.setQueryEntities(Collections.singletonList(queryEntity));

        cacheCfg.setReadThrough(true);
        //cacheCfg.setWriteThrough(true);
        cacheCfg.setWriteThrough(false); // 关闭通写
        cacheCfg.setWriteBehindEnabled(true); // 启用写回（关键配置）

        // 写回高级配置（可选）
        cacheCfg.setWriteBehindFlushSize(10000); // 每 10000 条记录刷新一次
        cacheCfg.setWriteBehindFlushFrequency(3000L); // 每 3 秒刷新一次
        cacheCfg.setWriteBehindFlushThreadCount(6); // 使用 6 个线程执行数据库写入

        CacheJdbcPojoStoreFactory<RiskLogKey, RiskLog> factory = new CacheJdbcPojoStoreFactory<>();
        //factory.setDialect(new BasicJdbcDialect());
        factory.setDataSourceFactory(new PostgresDataSourceFactory());

        JdbcType personType = CacheMapper.createJDBCType(RiskLogKey.class, RiskLog.class, "public", "t_risk");

        factory.setTypes(new JdbcType[]{personType});
        cacheCfg.setCacheStoreFactory(factory);

        riskCache = ignite.getOrCreateCache(cacheCfg);
    }

    public RiskLog getRiskById(RiskLogKey key) {
        return riskCache.get(key);
    }

    public void putRisk(final RiskLogKey key, final RiskLog value) {
        //userCache.put(user.getId(), user);

        // 使用put写入数据
        riskCache.put(key, value);
    }

    /*public void testFilter() {
        Query<Cache.Entry<RiskLogKey, RiskLog>> qry = new ScanQuery<RiskLogKey, RiskLog>(
                (k, v) -> v.getAge() > 10
        );

        try (QueryCursor<Cache.Entry<RiskLogKey, RiskLog>> cur = riskCache.query(qry)) {
            for (Cache.Entry<RiskLogKey, RiskLog> entry : cur) {
                // Process the entry ...
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }*/


}




