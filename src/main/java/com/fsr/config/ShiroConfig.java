package com.fsr.config;
import com.fsr.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //创建shiroFilter,负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier(value="defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager ){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap();

        //配置系统受限资源
        //配置系统公共资源
        // 静态资源的处理
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/user/login","anon");//anon 设置为公共资源  放行资源放在下面
        filterChainDefinitionMap.put("/user/register","anon");//anon 设置为公共资源  放行资源放在下面
        filterChainDefinitionMap.put("/register.jsp","anon");//anon 设置为公共资源  放行资源放在下面
        filterChainDefinitionMap.put("/user/getImage","anon");
        filterChainDefinitionMap.put("/**","authc");//authc 请求这个资源需要认证和授权
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    //自定义安全管理器
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier(value="realm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
    return defaultWebSecurityManager;
    }
    //自定义shiro的realm
    @Bean
    public Realm realm(){
        CustomerRealm  customerRealm=new CustomerRealm();
        //设置hashed凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置md5加密
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        return customerRealm;
    }

}
