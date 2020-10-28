package com.qianfeng.demoredis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * creator：Administrator
 * date:2019/11/18
 * 配置一个redis的配置程序
 */
@SpringBootConfiguration
public class RedisConfig {
    Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Resource
    Environment environment;

    // redis的客户端程序
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        String redisIp = "121.196.57.23";// 实际工作种这几个ip是不一样的，端口范尔是一样的都是6379

        String[] activeProfiles = environment.getActiveProfiles();
        if(activeProfiles!=null){
            for (String ac : activeProfiles) {
                if("dev".equals(ac)){
                    redisIp="121.196.57.23";
                }else if("pro".equals(ac)){
                    redisIp="正式生产环境的ip";
                }

            }
        }

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setPassword("java");// redis的集群密码
        redisClusterConfiguration.clusterNode(redisIp, 6371);// redis的节点的ip和端口号
        redisClusterConfiguration.clusterNode(redisIp, 6372);
        redisClusterConfiguration.clusterNode(redisIp, 6373);
        redisClusterConfiguration.clusterNode(redisIp, 6374);
        redisClusterConfiguration.clusterNode(redisIp, 6375);
        redisClusterConfiguration.clusterNode(redisIp, 6376);

        // 还需要一些属性
        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(1000);      //  设置最大空闲连接数8
        genericObjectPoolConfig.setMaxTotal(1000); // 设置最大连接数1000
        genericObjectPoolConfig.setMinIdle(1);    // 设置最小空闲连接数1
        genericObjectPoolConfig.setMaxWaitMillis(-1); // 设置最大等待时间无限制
        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder = LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofSeconds(60));// 默认就是60秒命令超时
//        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
        logger.warn("##### RRRRRRRRRRRRRRRRRRRRRRRRRRRRRR ######redis的地址是：" + redisIp);
        return lettuceConnectionFactory;
    }

    // 还需要准备一个 RedisTemplate 用来操作redis的数据
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);// 设置redis连接工厂

        redisTemplate.setKeySerializer(new StringRedisSerializer());// 设置key序列化对象类型（就是把key设置成字符串处理）
//        GenericJackson2JsonRedisSerializer // 用这个，但是性能稍微差点/
        // 设置value序列化对象类型 记住，一般不要去设置（），特别是在分布式session共享的时候，如果设置了，那么session转换可能会产生异常
        // 不设置就采用JDK自带的序列化工具，就是最好的！
//       redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());// 设置value序列化对象类型
        return redisTemplate;
    }

}
