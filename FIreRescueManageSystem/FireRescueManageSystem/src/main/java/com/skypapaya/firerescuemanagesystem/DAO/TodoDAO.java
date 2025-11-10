package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.TodoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TodoDAO {
    // 1. 查询所有待办事项 -> 查询指定用户的待办事项
    List<TodoDO> findAllByUserId(@Param("userId") Long userId);

    // 2. 新增一个待办事项 (不变, userId 将在 TodoDO 对象中)
    int insertTodo(TodoDO todo);

    // 3. 更新一个待办事项的状态 (需要 userId 来保证安全)
    int updateTodoStatus(TodoDO todo);

    // 4. 删除一个待办事项 (需要 userId 来保证安全)
    int deleteTodo(@Param("id") Long id, @Param("userId") Long userId);

    // 5. 更新所有事项的状态 (需要 userId)
    int updateAllStatus(@Param("status") Boolean status, @Param("userId") Long userId);

    // 6. 删除所有事项 (需要 userId)
    int deleteAllByUserId(@Param("userId") Long userId);
}