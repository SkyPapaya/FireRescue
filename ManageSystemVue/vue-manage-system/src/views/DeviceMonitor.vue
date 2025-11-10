<template>
  <el-container class="layout-container-demo" style="height: 100%">
    <el-container>
      <div style="display:block">
        <el-table
            :data="state.tableData"
            style="width: 100%; font-size: 20px; display: block; color: black"
            :row-class-name="tableRowClassName"
            :header-cell-style="{color:'black'}"
        >
          <el-table-column prop="device_name" label="设备名称" width="180px" align="center"/>
          <el-table-column prop="humidity" label="湿度" width="180px" align="center" style="color: black"/>
          <el-table-column prop="temperature" label="最高温度" width="180px" align="center"/>
          <el-table-column prop="fire" label="是否存在火源" width="180px" align="center"/>
          <el-table-column prop="smoke" label="烟雾浓度" width="180px" align="center"/>
          <el-table-column prop="co" label="有害气体浓度" width="180px" align="center"/>
          <el-table-column prop="risk" label="火灾风险" width="180px" align="center"/>
        </el-table>
      </div>

    </el-container>
    <div class="example-pagination-block"
         style="padding-left: 10px; width: 100px;height: 50px; position: absolute; left: 10px ; bottom: 250px ">
      <div class="example-demonstration"></div>
      <el-pagination layout="prev, pager, next" :total="50" page-count="10" @current-change="handleCurrentChange"/>
    </div>

  </el-container>
</template>

<script setup lang="ts">
import {reactive, ref, onMounted} from "vue";
// (移除了 useRoute)
import service from "../utils/request";
import {ElNotification, ElMessage} from 'element-plus'; // 导入 ElMessage

// (接口定义保持不变) [cite: 750-762]
interface environment {
  deviceName: string; // 确保接口包含此字段
  humidity: number;
  temperature: number;
  fire: number;
  smoke: number;
  co: number;
  risk: number;
}

// (状态变量保持不变) [cite: 750-762]
let humidity;
let temperature;
let fire;
let smoke;
let co;
let risk;
const state = reactive({
  tableData: [] as environment[], // 明确类型
  form: {},
});
const activeIndex = ref("1");
const activeIndex2 = ref("1");
const count = ref(0);

// (表格行样式函数保持不变) [cite: 753-754]
const tableRowClassName = ({
                             row,
                             rowIndex,
                           }: {
  row: environment;
  rowIndex: number;
}) => {
  if (row.risk === 1 || row.co > 20 || row.fire === 1) {
    return "warning-row";
  }
  return "";
};

// 3. 修复 load 函数
const load = () => {
  // ⬇️ ⬇️ ⬇️ 使用新的接口路径 ⬇️ ⬇️ ⬇️
  service.get(`/environment/getLatestFromAllDevices`).then((res) => {

    if (res.code === '200') {
      // 现在 res.data 是一个包含所有设备最新数据的数组
      state.tableData = res.data;

      // 检查是否有数据，防止访问 null
      if (state.tableData.length > 0) {
        // (此处的逻辑现在意义不大，因为有多行数据，但我们暂时保留它以触发警报)
        // (您可以选择一个特定的设备或聚合数据来触发警报)
        const lastDeviceData = state.tableData[state.tableData.length - 1];
        humidity = lastDeviceData.humidity;
        temperature = lastDeviceData.temperature;
        fire = lastDeviceData.fire;
        smoke = lastDeviceData.smoke;
        co = lastDeviceData.co;
        risk = lastDeviceData.risk;
      } else {
        humidity = null;
        temperature = null;
        fire = null;
        smoke = null;
        co = null;
        risk = null;
      }
    } else {
      ElNotification({ title: '提示', message: res.message || `加载数据失败`, type: 'warning' });
      state.tableData = []; // 清空数据
    }
  }).catch(err => {
    ElMessage.error("连接后端失败");
    state.tableData = []; // 清空数据
  });
};

// (onMounted 保持不变) [cite: 750-762]
onMounted(() => {
  load();
});

// (setInterval 保持不变)
setInterval(() => {
  load(); // 每次都重新加载所有设备的最新数据

  // (警报逻辑现在应该遍历所有设备)
  let fireAlert = false;
  for (const device of state.tableData) {
    if (device.temperature > 50 || device.fire === 1 || device.co > 20 || device.smoke > 80 || device.risk === 1) {
      fireAlert = true;
      break; // 只要有一个设备报警就跳出
    }
  }

  if (fireAlert) {
    ElNotification({
      title: 'Warning',
      message: '有设备检测到火情',
      type: 'error',
    })
  }
}, 10000); // (刷新频率改为 10 秒)

// (分页处理函数保持不变)
const handleCurrentChange = (val) => {
  // 注意：您目前的分页是前端分页 (total="50")
  // 您的 load() 函数会获取所有数据，这可能不是您想要的
  // 理想情况下，您应该将 handleCurrentChange 链接到后端的 /userPage [cite: 89-91]
  // 但对于环境数据，我们目前只显示最新数据，分页可能不需要
}
</script>

<style>
.el-table .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
  background-color: #cc0033;
  color: #cccccc;
}

.el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  color: var(--el-text-color-primary);
  width: 100%;
}

.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
}

.layout-container-demo .el-menu {
  border-right: none;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>