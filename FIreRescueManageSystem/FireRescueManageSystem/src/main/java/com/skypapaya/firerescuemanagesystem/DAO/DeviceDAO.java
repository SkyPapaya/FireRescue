package com.skypapaya.firerescuemanagesystem.DAO;

import com.skypapaya.firerescuemanagesystem.DO.DeviceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceDAO {

    /**
     * 查询所有设备
     * @return 设备列表
     */
    List<DeviceDO> findAllDevices();

    /**
     * 插入一个新设备
     * @param device 设备对象
     * @return 影响的行数
     */
    int insertDevice(DeviceDO device);

    /**
     * 根据ID删除设备
     * @param id 设备ID
     * @return 影响的行数
     */
    int deleteDeviceById(@Param("id") Long id);
}