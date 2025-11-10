package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.MessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDAO {

    /**
     * 查询所有消息
     */
    List<MessageDO> findAll();

    /**
     * (可选) 插入新消息
     */
    int insertMessage(MessageDO message);

    /**
     * 根据ID更新单个消息的状态
     * (用于: 标为已读、删除到回收站、还原)
     */
    int updateStatusById(@Param("id") Long id, @Param("status") int status);

    /**
     * 批量更新状态
     * (用于: 全部标为已读、删除全部)
     */
    int updateAllStatus(@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    /**
     * 按状态删除
     * (用于: 清空回收站)
     */
    int deleteByStatus(@Param("status") int status);
}