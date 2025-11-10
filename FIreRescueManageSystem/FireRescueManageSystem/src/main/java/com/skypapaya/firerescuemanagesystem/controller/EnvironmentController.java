package com.skypapaya.firerescuemanagesystem.controller;

import com.skypapaya.firerescuemanagesystem.DAO.EnvironmentDAO;
import com.skypapaya.firerescuemanagesystem.DO.EnvironmentDO;
import com.skypapaya.firerescuemanagesystem.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://loclahost:5173")
@RestController
public class EnvironmentController {
    @Autowired
    EnvironmentDAO environmentDAO;

    @GetMapping("environment/getTheLatest")
    // ⬇️ 1. 添加 @RequestParam ⬇️
    public Result getTheLasted(@RequestParam String deviceName) {
        Result result = new Result();

        // ⬇️ 2. 传递参数 ⬇️
        List<EnvironmentDO> environmentDOS = environmentDAO.getTheLatest(deviceName);

        if (environmentDOS == null || environmentDOS.isEmpty()) { // ⬅️ 3. 增加 isEmpty 检查
            result.setCode("404");
            result.setMessage("没有找到设备[" + deviceName + "]的数据");
            return result;
        }
        result.setData(environmentDOS);
        return Result.success(environmentDOS);
    }
    @GetMapping("environment/getLatestFromAllDevices")
    public Result getLatestFromAll() {
        List<EnvironmentDO> environmentDOS = environmentDAO.getLatestFromAllDevices();
        if (environmentDOS == null || environmentDOS.isEmpty()) {
            return Result.error("404", "没有找到任何环境数据");
        }
        return Result.success(environmentDOS);
    }

    //插入数据
    @ResponseBody
    @PostMapping("environment/insertEnvironment")
    public Result insertEnvironmentDO(@RequestBody EnvironmentDO environment) {
        EnvironmentDO environmentDO = new EnvironmentDO();
        int res = environmentDAO.insertEnvironmentDO(environmentDO);
        if (res == 1) {
            return Result.success("插入成功");
        } else {
            return Result.error("插入失败");
        }
    }
}
