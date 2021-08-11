package com.fsr.shiro.realms;

import com.fsr.entity.Perms;
import com.fsr.entity.User;
import com.fsr.service.UserService;
import com.fsr.utils.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

//自定义realm
public class CustomerRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取身份信息
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("调用授权验证: "+primaryPrincipal);
        //根据主身份信息获取角色 和 权限信息
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");
        User user = userService.findRolesByUserName(primaryPrincipal);
        System.out.println("user:"+user);
        //授权角色信息
        if(!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role->{
                //添加角色信息
                simpleAuthorizationInfo.addRole(role.getName());
                //循环遍历该角色的权限信息，并加入到simpleAuthorizationInfo中
                List<Perms> perms = userService.findPermsByRoleId(role.getId());
                System.out.println("perms:"+perms);
                if(!CollectionUtils.isEmpty(perms) && perms.get(0)!=null ){
                    perms.forEach(perm->{
                        simpleAuthorizationInfo.addStringPermission(perm.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("=============");
        //从传过来的token获取到的用户名
        String principal = (String) token.getPrincipal();
        //在工厂中获取userService
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");
        User user=userService.findByUserName(principal);
        System.out.println("用户名"+principal);
        //假设是从数据库获得的 用户名，密码
        if (!ObjectUtils.isEmpty(user)){
           SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),
                   ByteSource.Util.bytes(user.getSalt()), this.getName());
           return simpleAuthenticationInfo;
        }
        return null;
    }
}
