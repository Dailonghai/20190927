package com.lanxin.shirorealm;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aptx4869 on 2019/9/26.
 */
@Configuration
public class ShiroConfig {

    /* 自定义realm */
    @Bean
    public ShiroRealm shiroRealm(){

        ShiroRealm shiroRealm=new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /* 凭证匹配器（由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了） */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){

        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");/* 散列算法，这里使用MD5算法 */
        hashedCredentialsMatcher.setHashIterations(100);/* 散列的次数 */
        return hashedCredentialsMatcher;
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm,RedisCacheManager redisCacheManager,SessionManager sessionManager){

        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        /* 设置realm */
        securityManager.setRealm(shiroRealm);
        /* 自定义缓存实现 使用Redis */
        securityManager.setCacheManager(redisCacheManager);
        /* 自定义session管理 使用Redis */
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String,String> map=new HashMap<>();

        map.put("/login","anon");
        map.put("/logout","anon");
        map.put("/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        /* 没登录 */
        shiroFilterFactoryBean.setLoginUrl("/hello");

        return shiroFilterFactoryBean;
    }

    /* 支持controller层注解，实现权限控制 */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){

        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /* 开启spring aop注解支持，使用代理方式，所以需要开启代码支持 */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){

        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor=new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /* RedisSessionDAO shiro sessionDAO层的实现，通过Redis，使用的是shiro-redis开源插件 */
    @Bean
    public RedisSessionDAO redisSessionDao(RedisManager redisManager){

        RedisSessionDAO redisSessionDao=new RedisSessionDAO();
        redisSessionDao.setRedisManager(redisManager);
        return redisSessionDao;
    }

    /* Session Manager 使用的是shiro-redis开源插件 */
    @Bean
    public DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDao){

        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDao);
        return sessionManager;
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    /* 配置shiro RedisManager
     * 使用的是shiro-redis开源插件
     * */
    @Bean
    public RedisManager redisManager(){

        RedisManager redisManager=new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        return redisManager;
    }

    /* cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     * */
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager){

        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

}
