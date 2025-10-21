package org.ruphile.bigdata.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.compute.ComputeTask;
import org.apache.ignite.configuration.CacheConfiguration;
import org.ruphile.bigdata.PostgresDataSourceFactory;
import org.ruphile.bigdata.entity.User;
import org.ruphile.bigdata.entity.UserKey;
import org.ruphile.bigdata.task.CharacterTask;
import org.ruphile.bigdata.util.CacheMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;

/*
CREATE TABLE t_user (
    id VARCHAR(32) NOT NULL,
    name VARCHAR(32),
    age INTEGER,
    email VARCHAR(64),
    phone VARCHAR(32),
    address VARCHAR(128),
    gender INTEGER,
    PRIMARY KEY (id)
);
 */
@Service
public class UserService {
    @Resource
    private Ignite ignite;

    private IgniteCache<UserKey, User> userCache;

    @PostConstruct
    public void init() {
        String cacheName = User.class.getSimpleName().toLowerCase();
        if (ignite.cacheNames().contains(cacheName)) {
            userCache = ignite.cache(cacheName);
            compute();
            return;
        }

        CacheConfiguration<UserKey, User> cacheCfg = new CacheConfiguration<>();
        cacheCfg.setName(cacheName);
        cacheCfg.setSqlSchema("BASE");
        QueryEntity queryEntity = CacheMapper.createTableEntity(UserKey.class, User.class);
        cacheCfg.setQueryEntities(Collections.singletonList(queryEntity));

        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);

        CacheJdbcPojoStoreFactory<UserKey, User> factory = new CacheJdbcPojoStoreFactory<>();
        //factory.setDialect(new BasicJdbcDialect());
        factory.setDataSourceFactory(new PostgresDataSourceFactory());

        JdbcType personType = CacheMapper.createJDBCType(UserKey.class, User.class, "public", "t_user");

        factory.setTypes(new JdbcType[]{personType});
        cacheCfg.setCacheStoreFactory(factory);

        userCache = ignite.getOrCreateCache(cacheCfg);
    }

    public User getUserById(UserKey key) {
        return userCache.get(key);
    }

    public void putUser(final UserKey key, final User value) {
        //userCache.put(user.getId(), user);

        // 使用put写入数据
        userCache.put(key, value);
    }

    /*public void testFilter() {
        Query<Cache.Entry<UserKey, Use>> qry = new ScanQuery<UserKey, Use>(
                (k, v) -> v.getAge() > 10
        );

        try (QueryCursor<Cache.Entry<UserKey, Use>> cur = riskCache.query(qry)) {
            for (Cache.Entry<UserKey, Use> entry : cur) {
                // Process the entry ...
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }*/

    public void compute() {
        ignite.compute().execute(new CharacterTask(), "public static void main(String[] args)");
    }


}




