package com.vic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.concurrent.ExecutionException;

/**
 * @author 罗利华
 * date: 2019/7/25 09:29
 */
public class Lettuce02Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Syntax: redis://[password@]host[:port][/databaseNumber]
        RedisURI redisURI = RedisURI.builder().withHost("192.168.6.131").withPort(6379).build();

        RedisClient redisClient = RedisClient.create(redisURI);

        StatefulRedisConnection<String, String> connect = redisClient.connect();

        RedisCommands<String, String> sync = connect.sync();
        System.out.println(sync.get("name"));

        RedisAsyncCommands<String, String> async = connect.async();
        RedisFuture<String> rf = async.get("diana");

        rf.thenAccept(System.out::println);

        while(!rf.isDone()) {
            System.out.println(rf.get());
        }

        connect.close();
        redisClient.shutdown();
    }

}
