package com.vic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * pool基本使用
 * @author 罗利华
 * date: 2019/7/25 10:56
 */
public class Test1 {

    public static void main(String[] args) throws Exception {
        final RedisClient redisClient = RedisClient.create("redis://192.168.6.131:6379/0");

        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(() -> redisClient.connect(), new GenericObjectPoolConfig());

        try(StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisAsyncCommands<String, String> async = connection.async();
            async.multi();
            async.set("name", "khan");
            async.set("age", "20");
            async.exec();
        }
        pool.close();
        redisClient.shutdown();

    }

}
