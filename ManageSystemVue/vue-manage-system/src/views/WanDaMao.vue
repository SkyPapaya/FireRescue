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
import service from "../utils/request";
import {ElNotification} from 'element-plus'
import {color} from "echarts";

interface environment {
  humidity: number;
  temperature: number;
  fire: number;
  smoke: number;
  co: number;
  risk: number;
}

let humidity; //8
let temperature; //50
let fire; //0
let smoke; //8
let co; //20
let risk; //0
const state = reactive({
  tableData: [],
  form: {},
});
const activeIndex = ref("1");
const activeIndex2 = ref("1");
const count = ref(0);

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

// 请求后台数据
const load = () => {
  service.get('/environment/getTheLatest').then((res) => {
    const newData = res.data;
    // 清空表格数据
    state.tableData = [];
    // 将获得的五条数据添加到表格数据中
    for (let i = 0; i < newData.length; i++) {
      state.tableData.push(newData[i]);
    }
    // 更新数据到界面
    humidity = state.tableData[state.tableData.length - 1].humidity;
    temperature = state.tableData[state.tableData.length - 1].temperature;
    fire = state.tableData[state.tableData.length - 1].fire;
    smoke = state.tableData[state.tableData.length - 1].smoke;
    co = state.tableData[state.tableData.length - 1].co;
    risk = state.tableData[state.tableData.length - 1].risk;
  });
};

// 初始加载数据
onMounted(() => {
  load();
});

// 每三秒刷新一次数据
setInterval(() => {
  state.tableData.shift();
  state.tableData.shift();
  state.tableData.shift();
  state.tableData.shift();
  state.tableData.shift();
  load();
  if (humidity > 80 || temperature > 50 || fire === 1 || smoke > 80 || co > 20 || risk === 1) {
    ElNotification({
      title: 'Warning',
      message: '有火情',
      type: 'error',
    })
  }
}, 10000);

// 处理分页变化
const handleCurrentChange = (val) => {
  // 这里添加处理分页变化的逻辑，你可以在这个函数中重新加载数据
  // 例如：每次加载五条数据，根据当前页数计算加载的起始索引，然后获取对应数据
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
