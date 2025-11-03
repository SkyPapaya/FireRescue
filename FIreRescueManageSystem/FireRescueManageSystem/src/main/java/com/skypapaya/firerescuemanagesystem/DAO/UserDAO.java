package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDAO {

    public List<UserDO> findAll();

    int insertUser(UserDO user);

    int delete(@Param("id") Long id);

    int registerUser(UserDO user);

    /**
     * 根据用户名查询用户
     * @param name 用户名
     * @return UserDO 对象，如果不存在则为 null
     */
    UserDO findByName(@Param("name") String name);

    //分页查询的实现
    @Select("select * from user limit #{page}, #{size}")
    List<UserDO> page(int page, int size);

    //返回数据库的总条数
    @Select("SELECT COUNT(*) from user")
    int count();

}
