package com.lanxin.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by aptx4869 on 2019/9/26.
 */
@Mapper
public interface IShiroDao {

    String selectPassByname(String username);

    List<String> selectRoleByname(String username);

    List<String> selectPermByname(String username);
}
