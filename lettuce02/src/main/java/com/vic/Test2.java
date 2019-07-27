package com.vic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * pool集群使用
 * @author 罗利华
 * date: 2019/7/25 10:56
 */
public class Test2 {

    public static void main(String[] args) throws Exception {

        // Syntax: redis://[password@]host[:port]
        RedisClusterClient clusterClient = RedisClusterClient.create("redis://192.168.6.131:6379/1");

        GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(() -> clusterClient.connect(), new GenericObjectPoolConfig());

        // execute work
        try (StatefulRedisClusterConnection<String, String> connection = pool.borrowObject()) {
            connection.sync().set("birthday", "20190101");
//            connection.sync().blpop(10, "list");
        }

        // terminating
        pool.close();
        clusterClient.shutdown();

    }

}
