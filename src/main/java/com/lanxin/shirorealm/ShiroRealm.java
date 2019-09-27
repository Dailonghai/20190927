package com.lanxin.shirorealm;

import com.lanxin.dao.IShiroDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by aptx4869 on 2019/9/26.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private IShiroDao iShiroDao;

    /* 授权 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username=(String)principalCollection.getPrimaryPrincipal();
        List<String> roles=iShiroDao.selectRoleByname(username);
        List<String> permission=iShiroDao.selectPermByname(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /* 认证 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username=(String)authenticationToken.getPrincipal();
        String password=iShiroDao.selectPassByname(username);
        if(password!=null){
            SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(username,password,ByteSource.Util.bytes(username),"");
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
