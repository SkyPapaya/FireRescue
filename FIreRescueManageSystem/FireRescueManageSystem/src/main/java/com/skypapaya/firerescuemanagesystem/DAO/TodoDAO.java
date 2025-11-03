package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.TodoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TodoDAO {
    // 1. 查询所有待办事项
    List<TodoDO> findAll();

    // 2. 新增一个待办事项
    int insertTodo(TodoDO todo);

    // 3. 更新一个待办事项的状态
    int updateTodoStatus(TodoDO todo);

    // 4. 删除一个待办事项
    int deleteTodo(@Param("id") Long id);

    // 5. 更新所有事项的状态 (全部完成)
    int updateAllStatus(@Param("status") Boolean status);

    // 6. 删除所有事项 (清空)
    int deleteAll();
}