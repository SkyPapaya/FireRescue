<template>
  <div>
    <div class="container">
      <div class="search-box">
        <el-button type="warning" :icon="CirclePlusFilled" @click="handleAdd">
          新增设备
        </el-button>
      </div>
      <el-table
          :data="deviceList"
          border
          class="table"
          header-cell-class-name="table-header"
      >
        <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="设备名称" align="center"></el-table-column>
        <el-table-column prop="location" label="位置" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" align="center"></el-table-column>
        <el-table-column prop="gmtCreated" label="创建时间" align="center"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button
                type="danger"
                size="small"
                :icon="Delete"
                @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>

    <el-dialog title="新增设备" v-model="dialogVisible" width="30%">
      <el-form :model="newDeviceForm" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="newDeviceForm.name"></el-input>
        </el-form-item>
        <el-form-item label="设备位置">
          <el-input v-model="newDeviceForm.location"></el-input>
        </el-form-item>
        <el-form-item label="初始状态">
          <el-input v-model="newDeviceForm.status" placeholder="例如: online"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAddDevice">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="deviceManagement">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, CirclePlusFilled } from "@element-plus/icons-vue";
import service from '../utils/request';

// 设备接口类型
interface DeviceItem {
  id: number;
  name: string;
  location: string;
  status: string;
  gmtCreated: string;
}

// 设备列表
const deviceList = ref<DeviceItem[]>([]);

// 对话框可见性
const dialogVisible = ref(false);

// 新设备表单
const newDeviceForm = reactive({
  name: "",
  location: "",
  status: "online",
});

// 从后端获取设备数据
const getDeviceData = async () => {
  try {
    const res = await service.get('/device/all'); // 调用后端接口
    if (res.code === '200') {
      deviceList.value = res.data;
    } else {
      ElMessage.error(res.message || '加载设备列表失败');
    }
  } catch (error) {
    ElMessage.error("连接后端失败");
  }
};

// 页面加载时执行
onMounted(() => {
  getDeviceData();
});

// 处理删除
const handleDelete = (id: number) => {
  ElMessageBox.confirm("确定要删除这个设备吗？", "提示", {
    type: "warning",
  })
      .then(async () => {
        // 确认删除
        try {
          const res = await service.delete(`/device/delete/${id}`);
          if (res.code === '200') {
            ElMessage.success("删除成功");
            getDeviceData(); // 刷新列表
          } else {
            ElMessage.error(res.message || '删除失败');
          }
        } catch (error) {
          ElMessage.error("请求失败");
        }
      })
      .catch(() => {
        // 取消删除
      });
};

// 处理新增（打开弹窗）
const handleAdd = () => {
  // ========== 在这里添加权限检查 ==========
  const authority = localStorage.getItem('ms_authority');
  if (authority !== 'admin') {
    ElMessage.error('权限不够，只有管理员才能新增设备');
    return; // 阻止函数继续执行
  }
  // ======================================

  // 重置表单
  newDeviceForm.name = "";
  newDeviceForm.location = "";
  newDeviceForm.status = "online";
  dialogVisible.value = true;
};

// 提交新增设备
const submitAddDevice = async () => {
  try {
    const res = await service.post('/device/add', newDeviceForm);
    if (res.code === '200') {
      ElMessage.success("添加成功");
      dialogVisible.value = false;
      getDeviceData(); // 刷新列表
    } else {
      ElMessage.error(res.message || '添加失败');
    }
  } catch (error) {
    ElMessage.error("请求失败");
  }
};
</script>

<style scoped>
.search-box {
  margin-bottom: 20px;
}
</style>