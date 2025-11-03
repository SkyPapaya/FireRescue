package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.UserDAO;
import com.skypapaya.firerescuemanagesystem.DO.UserDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    //返回所有user对象
    @GetMapping("/users")
    public Result findAll() {
        List<UserDO> users = userDAO.findAll();
        return Result.success(users);
    }

    //插入用户，根据user对象
    @PostMapping("/insertUser")
    @ResponseBody
    public int insertUser(@RequestBody UserDO user) {
        return userDAO.insertUser(user);
    }

    //根据id删除用户
    @DeleteMapping("/deleteUser{id}")
    public int delete(@PathVariable Long id) {
        return userDAO.delete(id);
    }

    @GetMapping("/userPage")
    public Result page(@RequestParam("pageNum") int pageNum, @RequestParam("size") int size) {
        pageNum = (pageNum - 1) * size;
        Map<String, Object> res = new HashMap<>();
        res.put("total", userDAO.count());
        res.put("data", userDAO.page(pageNum, size));
        return Result.success(res);
    }


}
