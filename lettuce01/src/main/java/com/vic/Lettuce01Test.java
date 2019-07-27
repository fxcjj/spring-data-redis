package com.vic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * @author 罗利华
 * date: 2019/7/25 09:29
 */
public class Lettuce01Test {

    public static void main(String[] args) {
        // Syntax: redis://[password@]host[:port][/databaseNumber]
        RedisClient redisClient = RedisClient.create("redis://192.168.6.131:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("b", "monster needs love");

        connection.close();
        redisClient.shutdown();
    }

}
