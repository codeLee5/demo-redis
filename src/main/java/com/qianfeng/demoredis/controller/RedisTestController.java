package com.qianfeng.demoredis.controller;

import com.qianfeng.demoredis.pojo.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * creator：Administrator
 * date:2019/11/18
 */
@RestController
public class RedisTestController {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/")
    Object index() {
        // 存字符串

        redisTemplate.opsForValue().set("name", "张三");// 存字符串
        Object name = redisTemplate.opsForValue().get("name");
        String x = (String) name;
        System.err.println(x);
        // 存set
        Set<String> set = new HashSet<>(Arrays.asList("1", "2", "3"));
        redisTemplate.opsForValue().set("set", set);// 存字符串


        Object set1 = redisTemplate.opsForValue().get("set");


        System.err.println(set1);

        // 总结什么都能存，存的是有序的，取出来就是有序的，所有，那些数据类型有好大个意义？

        // 那么再来，如果要设置过期时间呢？

        // 例如设置验证码过期时间为60秒
        // setex
        redisTemplate.opsForValue().set("code", "333444", 5, TimeUnit.SECONDS);

        // setnx(分布式锁后期需要)
        redisTemplate.opsForValue().setIfAbsent("name", "李四");// setnx:如果不存在key就设置，如果存在就不设置
        Object name3 = redisTemplate.opsForValue().get("name");

        redisTemplate.opsForValue().setIfPresent("name", "王五"); // 如果存在就设置key,如果不存在就不设置
        Object name2 = redisTemplate.opsForValue().get("name");

        // 模糊查询key
        Set<String> keys = redisTemplate.keys("t_user_*");

        // 批量查询
        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        redisTemplate.delete(keys);// 批量或者单个删除

        System.err.println(name2);


        return "你好，美女！";
    }

}
