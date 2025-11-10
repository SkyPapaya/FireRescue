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
        
        <el-card shadow="hover">
          <template #header>
            <div class="clearfix">
              <span>待办事项</span>
              <el-button style="float: right; padding: 3px 0" text @click="handleAdd">
                添加
              </el-button>
            </div>
          </template>

          <div class="todo-list-wrapper">
            <el-table :show-header="false" :data="todoList" style="width: 100%">
              
              <el-table-column width="40">
                <template #default="scope">
                  <el-checkbox 
                    v-model="scope.row.status" 
                    @change="handleStatusChange(scope.row)"
                  ></el-checkbox>
                </template>
              </el-table-column>

              <el-table-column>
                <template #default="scope">
                  <div class="todo-item-wrapper">
                    <div
                        class="todo-item"
                        :class="{'todo-item-del': scope.row.status}"
                    >
                      {{ scope.row.title }}
                    </div>
                    <el-icon class="todo-delete-icon" @click="handleDelete(scope.row)">
                        <Close />
                    </el-icon>
                  </div>
                </template>
              </el-table-column>
              
            </el-table>

            <div class="todo-buttons">
              <el-button type="success" size="small" @click="handleCompleteAll">全部完成</el-button>
              <el-button type="danger" size="small" @click="handleDeleteAll">删除所有</el-button>
            </div>
          </div>
          
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

    <el-dialog v-model="dialogVisible" title="添加待办事项" width="30%">
      <el-input
          v-model="newTodoTitle"
          placeholder="请输入待办事项..."
          @keyup.enter="handleAddItem"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAddItem">
            确 定
          </el-button>
        </span>
      </template>
    </el-dialog>
    
  </div>
</template>

<script setup lang="ts" name="dashboard">
import Schart from "vue-schart";
// 1. 导入 onMounted 用于页面加载时获取数据
import { reactive, ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import imgurl from "../assets/img/img.jpg";
// 2. 导入 service (axios)
import service from "../utils/request";

const name = localStorage.getItem("ms_username");
const role: string = name === "admin" ? "超级管理员" : "普通用户";
// ... (options 和 options2 的图表配置) ...
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

// 3. 将 todoList 从 reactive 改为 ref()
const todoList = ref([]); // 初始为空数组

// 辅助函数：获取当前用户名
const getUsername = () => {
  const username = localStorage.getItem("ms_username");
  if (!username) {
    ElMessage.error("未找到用户信息，请重新登录");
    // 可以在这里强制跳转到登录页
    return null;
  }
  return username;
};

// 4. 新增：从后端加载数据的函数 (已修改)
const loadTodos = async () => {
    const username = getUsername();
    if (!username) return;

    try {
        // [修改] service.get('/todo/all') ->
        const res = await service.get('/todo/all', {
            params: { username: username } //
        });
        if (res.code === '200') {
            todoList.value = res.data;
        } else {
            ElMessage.error(res.message || '加载待办事项失败');
        }
    } catch (e) {
        ElMessage.error('网络请求失败');
    }
};

// 5. 新增：在组件挂载时调用加载函数
onMounted(() => {
    loadTodos();
});


// --- "添加"功能逻辑 (已修改) ---
const dialogVisible = ref(false);
const newTodoTitle = ref('');
const handleAdd = () => {
    newTodoTitle.value = ''; 
    dialogVisible.value = true;
};
const handleAddItem = async () => {
    const username = getUsername();
    if (!username) return;

    if (!newTodoTitle.value) {
        ElMessage.warning('请输入待办事项内容');
        return;
    }
    try {
        // [修改] service.post('/todo/add', ...) ->
        const res = await service.post(
            '/todo/add', 
            { title: newTodoTitle.value }, // data
            { params: { username: username } } // config (params)
        );
        if (res.code === '200') {
            await loadTodos();
            ElMessage.success('添加成功');
        } else {
            ElMessage.error(res.message || '添加失败');
        }
        dialogVisible.value = false;
    } catch (e) {
        ElMessage.error('添加失败');
    }
};

// --- "删除"和"清空"功能逻辑 (已修改) ---

// 8. 新增：处理 Checkbox 状态变更 (已修改)
const handleStatusChange = async (row) => {
    const username = getUsername();
    if (!username) return;

    try {
        // [修改] service.put('/todo/update', ...) ->
        await service.put(
            '/todo/update', 
            { id: row.id, status: row.status }, // data
            { params: { username: username } } // config (params)
        );
    } catch (e) {
        ElMessage.error('更新状态失败');
        row.status = !row.status;
    }
};

// 9. 修改：删除单个待办事项 (已修改)
const handleDelete = async (row) => {
    const username = getUsername();
    if (!username) return;

    try {
        // [修改] service.delete(...) ->
        await service.delete(`/todo/delete/${row.id}`, {
            params: { username: username }
        });
        await loadTodos();
        ElMessage.success('删除成功');
    } catch (e) {
        ElMessage.error('删除失败');
    }
};

// 12. 修改：全部完成 (已修改)
const handleCompleteAll = async () => {
    const username = getUsername();
    if (!username) return;

    try {
        // [修改] service.put('/todo/completeAll') ->
        await service.put('/todo/completeAll', null, { // data为null
             params: { username: username }
        });
        await loadTodos(); // 重新加载
    } catch (e) {
        ElMessage.error('操作失败');
    }
};

// 13. 修改：删除所有 (已修改)
const handleDeleteAll = () => {
    ElMessageBox.confirm(
        '你确定要删除所有待办事项吗？此操作不可恢复。',
        '警告',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )
    .then(async () => {
        const username = getUsername();
        if (!username) return;

        try {
            // [修改] service.delete('/todo/deleteAll') ->
            await service.delete('/todo/deleteAll', {
                params: { username: username }
            });
            await loadTodos(); // 重新加载
            ElMessage.success('已删除所有待行事项');
        } catch (e) {
            ElMessage.error('清空失败');
        }
    })
    .catch(() => {
         ElMessage.info('已取消删除');
    });
};
</script>

<style scoped>
.todo-list-wrapper {
  /* 这个高度是估算的卡片body区域高度。
    你可以根据需要调整 280px 这个值。
  */
  height: 280px; 
  overflow-y: auto;
}

.todo-item-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-delete-icon {
  cursor: pointer;
  color: #f56c6c;
  display: none;
}

.todo-item-wrapper:hover .todo-delete-icon {
  display: block;
}

.todo-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

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