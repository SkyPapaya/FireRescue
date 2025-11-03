package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.TodoDAO;
import com.skypapaya.firerescuemanagesystem.DO.TodoDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoDAO todoDAO;

    // 1. 获取所有待办事项
    @GetMapping("/all")
    public Result getAllTodos() {
        List<TodoDO> todos = todoDAO.findAll();
        return Result.success(todos);
    }

    // 2. 添加新事项
    @PostMapping("/add")
    public Result addTodo(@RequestBody TodoDO todo) {
        // 默认状态为 false (未完成)
        todo.setStatus(false);
        todoDAO.insertTodo(todo);
        return Result.success(todo); // 返回带 ID 的对象
    }

    // 3. 更新事项状态 (checkbox)
    @PutMapping("/update")
    public Result updateTodoStatus(@RequestBody TodoDO todo) {
        todoDAO.updateTodoStatus(todo);
        return Result.success();
    }

    // 4. 删除单个事项 (X)
    @DeleteMapping("/delete/{id}")
    public Result deleteTodo(@PathVariable Long id) {
        todoDAO.deleteTodo(id);
        return Result.success();
    }

    // 5. 全部完成
    @PutMapping("/completeAll")
    public Result completeAll() {
        todoDAO.updateAllStatus(true); // true = 完成
        return Result.success();
    }

    // 6. 删除所有
    @DeleteMapping("/deleteAll")
    public Result deleteAll() {
        todoDAO.deleteAll();
        return Result.success();
    }
}