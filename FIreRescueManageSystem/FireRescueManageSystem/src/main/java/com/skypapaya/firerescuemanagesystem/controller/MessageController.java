package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.MessageDAO;
import com.skypapaya.firerescuemanagesystem.DO.MessageDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageDAO messageDAO;

    // 获取消息列表 (0=未读, 1=已读, 2=回收站)
    @GetMapping("/list/{status}")
    public Result getMessagesByStatus(@PathVariable Integer status) {
        return Result.success(messageDAO.findByStatus(status));
    }

    // 更新单条消息状态 (标为已读 / 移入回收站 / 还原)
    @PutMapping("/status/{id}")
    public Result updateMessageStatus(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer newStatus = payload.get("status");
        messageDAO.updateStatusById(id, newStatus);
        return Result.success();
    }

    // 批量更新 (全部标为已读 / 全部移入回收站)
    @PutMapping("/status/all")
    public Result updateAllMessagesStatus(@RequestBody Map<String, Integer> payload) {
        Integer fromStatus = payload.get("fromStatus");
        Integer toStatus = payload.get("toStatus");
        messageDAO.updateAllStatus(fromStatus, toStatus);
        return Result.success();
    }

    // 清空回收站
    @DeleteMapping("/status/{status}")
    public Result deleteMessagesByStatus(@PathVariable Integer status) {
        // 确保只删除 status=2 (回收站) 的
        if (status == 2) {
            messageDAO.deleteByStatus(status);
            return Result.success();
        }
        return Result.error("403", "禁止的操作");
    }

    // 新增消息
    @PostMapping("/insertMessage")
    public Result addMessage(@RequestBody Map<String, String> payload) {
        MessageDO alertMessage = new MessageDO();
        alertMessage.setTitle(payload.get("title"));
        alertMessage.setMessageTime(LocalDateTime.now());
        alertMessage.setStatus(0); // 0 = 未读
        messageDAO.insertMessage(alertMessage);
        return Result.success();
    }
}