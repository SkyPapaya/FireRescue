package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.TodoDAO;
import com.skypapaya.firerescuemanagesystem.DAO.UserDAO;
import com.skypapaya.firerescuemanagesystem.DO.TodoDO;
import com.skypapaya.firerescuemanagesystem.DO.UserDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoDAO todoDAO;

    // 关键：注入 UserDAO 来根据 username 查找 user
    @Autowired
    private UserDAO userDAO;

    // 辅助方法：根据前端传来的 username 获取用户 ID
    private Long getUserId(String username) {
        UserDO user = userDAO.findByName(username);
        if (user == null) {
            throw new RuntimeException("用户不存在"); // 或者其他异常处理
        }
        return user.getId();
    }

    // 1. 获取所有待办事项 (已修改)
    @GetMapping("/all")
    public Result getAllTodos(@RequestParam("username") String username) {
        Long userId = getUserId(username);
        List<TodoDO> todos = todoDAO.findAllByUserId(userId);
        return Result.success(todos);
    }

    // 2. 添加新事项 (已修改)
    @PostMapping("/add")
    public Result addTodo(@RequestBody TodoDO todo, @RequestParam("username") String username) {
        Long userId = getUserId(username);
        todo.setUserId(userId); // 关键：设置 todo 归属
        todo.setStatus(false);
        todoDAO.insertTodo(todo);
        return Result.success(todo);
    }

    // 3. 更新事项状态 (已修改)
    @PutMapping("/update")
    public Result updateTodoStatus(@RequestBody TodoDO todo, @RequestParam("username") String username) {
        Long userId = getUserId(username);
        todo.setUserId(userId); // 关键：设置 userId 以便 update 语句验证
        todoDAO.updateTodoStatus(todo);
        return Result.success();
    }

    // 4. 删除单个事项 (已修改)
    @DeleteMapping("/delete/{id}")
    public Result deleteTodo(@PathVariable Long id, @RequestParam("username") String username) {
        Long userId = getUserId(username);
        todoDAO.deleteTodo(id, userId); // 传入 userId
        return Result.success();
    }

    // 5. 全部完成 (已修改)
    @PutMapping("/completeAll")
    public Result completeAll(@RequestParam("username") String username) {
        Long userId = getUserId(username);
        todoDAO.updateAllStatus(true, userId); // 传入 userId
        return Result.success();
    }

    // 6. 删除所有 (已修改)
    @DeleteMapping("/deleteAll")
    public Result deleteAll(@RequestParam("username") String username) {
        Long userId = getUserId(username);
        todoDAO.deleteAllByUserId(userId); // 调用新方法
        return Result.success();
    }
}