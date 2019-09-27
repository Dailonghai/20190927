package com.lanxin.controller;

import com.lanxin.dao.IShiroDao;
import com.lanxin.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aptx4869 on 2019/9/26.
 */
@RestController
public class UserController {

    @Autowired
    private IShiroDao iShiroDao;

    @RequestMapping(value = "/login")
    public Result login(String username,String password){

        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try {

            subject.login(token);
        } catch (IncorrectCredentialsException e2){
            Result result=new Result();
            result.setCode(10001);
            result.setMsg("密码错误");
            return result;
        } catch (AuthenticationException e) {
            Result result=new Result();
            result.setCode(10002);
            result.setMsg("账号不存在");
            return result;
        }
        return Result.ok();
    }

    @RequestMapping(value = "/logout")
    public Result logout(){

        Subject subject=SecurityUtils.getSubject();
        subject.logout();
        Result result=new Result();
        result.setCode(200);
        result.setMsg("退出成功");
        return result;
    }

    @RequestMapping(value = "/hello")
     public Result hello(){

        Result result=new Result();
        result.setCode(10000);
        result.setMsg("请先登录");
        return result;
    }

    @RequestMapping(value = "/add")
    public Result add(){

        return Result.ok("添加成功");
    }

    @RequestMapping(value = "/update")
    public Result update(){

        return Result.ok("修改成功");
    }

    @RequestMapping(value = "/delete")
    public Result delete(){

        return Result.ok("删除成功");
    }

    @RequestMapping(value = "/select")
    public Result select(){

        return Result.ok("查询成功");
    }
}
