package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.DeviceDAO;
import com.skypapaya.firerescuemanagesystem.DO.DeviceDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device") // 我们将所有设备相关的API放在 /device 路径下
public class DeviceController {

    @Autowired
    private DeviceDAO deviceDAO;

    /**
     * 获取所有设备列表
     * @return Result 包含设备列表
     */
    @GetMapping("/all")
    public Result getAllDevices() {
        List<DeviceDO> devices = deviceDAO.findAllDevices();
        return Result.success(devices);
    }

    /**
     * 添加一个新设备
     * @param device 从请求体中 JSON 自动转换的设备对象
     * @return Result 包含成功/失败信息
     */
    @PostMapping("/add")
    @ResponseBody
    public Result addDevice(@RequestBody DeviceDO device) {
        int result = deviceDAO.insertDevice(device);
        if (result > 0) {
            // 返回成功，并附带新插入的数据（可能包含自动生成的ID）
            return Result.success(device);
        } else {
            return Result.error("500", "添加设备失败");
        }
    }

    /**
     * 根据ID删除一个设备
     * @param id 从路径中获取的设备ID
     * @return Result 包含成功/失败信息
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteDevice(@PathVariable Long id) {
        int result = deviceDAO.deleteDeviceById(id);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("500", "删除失败，设备可能不存在");
        }
    }
}