<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20" style="height: 252px">
          <div class="user-info">
            <el-avatar :size="120" :src="imgurl"/>
            <div class="user-info-cont">
              <div class="user-info-name">{{ name }}</div>
              <div>{{ role }}</div>
            </div>
          </div>
          <div class="user-info-list">
            上次登录时间：
            <span>2024-10-01</span>
          </div>
          <div class="user-info-list">
            上次登录地点：
            <span>南京</span>
          </div>
        </el-card>
        <el-card shadow="hover" style="height: 252px">
          <template #header>
            <div class="clearfix">
              <span>火灾类型详细</span>
            </div>
          </template>
          厨房
          <el-progress :percentage="79.4" color="#42b983"></el-progress>
          电器
          <el-progress :percentage="14" color="#f1e05a"></el-progress>
          粉尘
          <el-progress :percentage="5.6"></el-progress>
          气体
          <el-progress :percentage="1" color="#f56c6c"></el-progress>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <el-icon class="grid-con-icon">
                  <User/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">1234</div>
                  <div>用户访问量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <el-icon class="grid-con-icon">
                  <ChatDotRound/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">321</div>
                  <div>系统消息</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <el-icon class="grid-con-icon">
                  <Goods/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">5000</div>
                  <div>火灾发生数量</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-card shadow="hover" style="height: 403px">
          <template #header>
            <div class="clearfix">
              <span>待办事项</span>
              <el-button style="float: right; padding: 3px 0" text
              >添加
              </el-button
              >
            </div>
          </template>

          <el-table :show-header="false" :data="todoList" style="width: 100%">
            <el-table-column width="40">
              <template #default="scope">
                <el-checkbox v-model="scope.row.status"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column>
              <template #default="scope">
                <div
                    class="todo-item"
                    :class="{
                    'todo-item-del': scope.row.status,
                  }"
                >
                  {{ scope.row.title }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
              ref="bar"
              class="schart"
              canvasId="bar"
              :options="options"
          ></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
              ref="line"
              class="schart"
              canvasId="line"
              :options="options2"
          ></schart>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts" name="dashboard">
import Schart from "vue-schart";
import {reactive} from "vue";
import imgurl from "../assets/img/img.jpg";

const name = localStorage.getItem("ms_username");
const role: string = name === "admin" ? "超级管理员" : "普通用户";

const options = {
  type: 'bar',
  title: {
    text: '造成火灾的原因'
  },
  bgColor: '#fbfbfb',
  labels: ['一季度', '二季度', '三季度', '四季度'],
  datasets: [
    {
      label: '电瓶车',
      fillColor: 'rgba(241, 49, 74, 0.5)',
      data: [234, 278, 270, 190, 230]
    },
    {
      label: '电器',
      data: [164, 178, 190, 135, 160]
    },
    {
      label: '粉尘',
      data: [144, 198, 150, 235, 120]
    }
  ]
};
const options2 = {
  type: 'line',
  title: {
    text: '火灾造成的经济损失'
  },
  bgColor: '#fbfbfb',
  labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
  datasets: [
    {
      label: '直接经济损失',
      data: [234, 278, 270, 190, 230, 550, 150, 592, 150, 589, 151, 111, 125]
    },
    {
      label: '间接经济损失',
      data: [164, 178, 190, 135, 160, 148, 11, 125, 114, 116, 31, 55]
    }

  ]
};
const todoList = reactive([
  {
    title: "木瓜小区火情",
    status: false,
  },
  {
    title: "44栋发生燃气泄露",
    status: false,
  },
  {
    title: "海绵小区消防栓维修",
    status: false,
  },

  {
    title: "海绵小区消防通道堵塞",
    status: true,
  },
  {
    title: "海绵小区消防设施检查",
    status: true,
  },
]);
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
</style>
