package com.vic;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * @author 罗利华
 * date: 2019/7/25 09:29
 */
public class Lettuce03Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisURI redisURI = RedisURI.builder().withHost("192.168.6.131").withPort(6379).build();

        RedisClient redisClient = RedisClient.create(redisURI);

        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisAsyncCommands<String, String> async = connect.async();
        RedisFuture<String> rf = async.get("c"); //blocking?测试打印为null，之后退出

        String s = rf.get();
        System.out.println(s);
        connect.close();
        redisClient.shutdown();
    }

}