package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.MessageDAO;
import com.skypapaya.firerescuemanagesystem.DO.MessageDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageDAO messageDAO;

    /**
     * 获取所有消息，并按状态 (unread, read, recycle) 分组
     * 这完全匹配 tabs.vue 的 'state' 结构
     */
    @GetMapping("/all")
    public Result getAllMessages() {
        List<MessageDO> allMessages = messageDAO.findAll();

        List<MessageDO> unread = new ArrayList<>();
        List<MessageDO> read = new ArrayList<>();
        List<MessageDO> recycle = new ArrayList<>();

        for (MessageDO msg : allMessages) {
            switch (msg.getStatus()) {
                case 0: // 未读
                    unread.add(msg);
                    break;
                case 1: // 已读
                    read.add(msg);
                    break;
                case 2: // 回收站
                    recycle.add(msg);
                    break;
            }
        }

        Map<String, List<MessageDO>> result = new HashMap<>();
        result.put("unread", unread);
        result.put("read", read);
        result.put("recycle", recycle);

        return Result.success(result);
    }

    /**
     * 标为已读 (对应 handleRead )
     * 0 -> 1
     */
    @PutMapping("/read/{id}")
    public Result markAsRead(@PathVariable Long id) {
        messageDAO.updateStatusById(id, 1);
        return Result.success();
    }

    /**
     * 删除到回收站 (对应 handleDel )
     * 1 -> 2
     */
    @PutMapping("/recycle/{id}")
    public Result moveToRecycle(@PathVariable Long id) {
        messageDAO.updateStatusById(id, 2);
        return Result.success();
    }

    /**
     * 从回收站还原 (对应 handleRestore)
     * 2 -> 1
     */
    @PutMapping("/restore/{id}")
    public Result restoreFromRecycle(@PathVariable Long id) {
        messageDAO.updateStatusById(id, 1);
        return Result.success();
    }

    /**
     * 全部标为已读 (对应 "全部标为已读" 按钮)
     * 0 -> 1
     */
    @PutMapping("/read-all")
    public Result markAllAsRead() {
        messageDAO.updateAllStatus(0, 1);
        return Result.success();
    }

    /**
     * 删除全部已读 (对应 "删除全部" 按钮)
     * 1 -> 2
     */
    @PutMapping("/recycle-all-read")
    public Result moveAllReadToRecycle() {
        messageDAO.updateAllStatus(1, 2);
        return Result.success();
    }

    /**
     * 清空回收站 (对应 "清空回收站" 按钮)
     * 2 -> (DELETE)
     */
    @DeleteMapping("/recycle-bin")
    public Result emptyRecycleBin() {
        messageDAO.deleteByStatus(2);
        return Result.success();
    }

    /**
     * 新增消息
     * 给外界提供一个新增消息的接口
     **/
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