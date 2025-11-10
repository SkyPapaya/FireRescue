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

    @PostMapping("/login")
    @ResponseBody
    public Result loginUser(@RequestBody UserDO loginRequest) {
        // 1. 根据用户名查询数据库
        UserDO dbUser = userDAO.findByName(loginRequest.getName());

        // 2. 检查用户是否存在
        if (dbUser == null) {
            return Result.error("404", "用户不存在");
        }

        // 3. 检查密码 (明文比较)
        if (!loginRequest.getPassword().equals(dbUser.getPassword())) {
            return Result.error("401", "密码错误");
        }

        // 4. 登录成功
        // 返回完整的用户信息，前端需要用 authority
        return Result.success(dbUser);
    }

    //编辑用户信息
    @PutMapping("/updateUser")
    @ResponseBody
    public Result updateUser(@RequestBody UserDO user) {
        // 检查 authority 字段，防止普通用户给自己提权
        // (这是一个可选的安全增强)
        // if (user.getAuthority() != null && user.getAuthority().equals("admin")) {
        //    // 在这里添加逻辑：检查当前操作者是否是 admin
        //    // 如果不是，则 return Result.error("403", "权限不足");
        // }

        int result = userDAO.updateUser(user);

        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("500", "更新失败，用户可能不存在");
        }
    }

    @PostMapping("/register")
    @ResponseBody
//    public Result registerUser(@RequestBody UserDO user) {
    public Result registerUser(@RequestBody Map<String, Object> body) {
        System.out.println("user:"+body.toString());
        // 检查用户名是否已存在 (可选但推荐)
        // ... (
        //   List<UserDO> users = userDAO.findByName(user.getName());
        //   if (users.size() > 0) {
        //       return Result.error("501", "用户名已存在");
        //   }
        // )
        UserDO user = new UserDO((String) body.get("name"), (String) body.get("eMail"), (String) body.get("phone"), (String) body.get("sex"), (String) body.get("password"), body.get("age") != null ? (int) body.get("age") : 0, (String) body.get("address"));
        System.out.println("user:"+user.toString());
        // 我们跳过加密，直接插入
        int result = userDAO.registerUser(user);

        if (result > 0) {
            return Result.success("注册成功");
        } else {
            return Result.error("500", "注册失败");
        }
    }

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
