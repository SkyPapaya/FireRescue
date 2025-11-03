package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.MessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface MessageDAO {
    // 插入新消息
    int insertMessage(MessageDO message);

    // 根据状态查询消息
    List<MessageDO> findByStatus(@Param("status") Integer status);

    // 更新单条消息的状态
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    // 批量更新状态 (如: 全部标为已读)
    int updateAllStatus(@Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);

    // 删除指定状态的所有消息 (如: 清空回收站)
    int deleteByStatus(@Param("status") Integer status);
}