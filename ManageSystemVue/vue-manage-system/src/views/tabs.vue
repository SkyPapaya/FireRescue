<template>
  <div class="container">
    <el-tabs v-model="message">
      <el-tab-pane :label="`未读消息(${state.unread.length})`" name="first">
        <el-table :data="state.unread" :show-header="false" style="width: 100%">
          <el-table-column>
            <template #default="scope">
              <span class="message-title">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="messageTime" width="180"></el-table-column> <el-table-column width="120">
          <template #default="scope">
            <el-button size="small" @click="handleRead(scope.row.id, scope.$index)">标为已读</el-button>
          </template>
        </el-table-column>
        </el-table>
        <div class="handle-row">
          <el-button type="primary" @click="handleReadAll">全部标为已读</el-button>
        </div>
      </el-tab-pane>

      <el-tab-pane :label="`已读消息(${state.read.length})`" name="second">
        <template v-if="message === 'second'">
          <el-table :data="state.read" :show-header="false" style="width: 100%">
            <el-table-column>
              <template #default="scope">
                <span class="message-title">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="messageTime" width="180"></el-table-column> <el-table-column width="120">
            <template #default="scope">
              <el-button type="danger" size="small" @click="handleDel(scope.row.id, scope.$index)">删除</el-button>
            </template>
          </el-table-column>
          </el-table>
          <div class="handle-row">
            <el-button type="danger" @click="handleRecycleAllRead">删除全部</el-button>
          </div>
        </template>
      </el-tab-pane>

      <el-tab-pane :label="`回收站(${state.recycle.length})`" name="third">
        <template v-if="message === 'third'">
          <el-table :data="state.recycle" :show-header="false" style="width: 100%">
            <el-table-column>
              <template #default="scope">
                <span class="message-title">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="messageTime" width="180"></el-table-column> <el-table-column width="120">
            <template #default="scope">
              <el-button size="small" @click="handleRestore(scope.row.id, scope.$index)">还原</el-button>
            </template>
          </el-table-column>
          </el-table>
          <div class="handle-row">
            <el-button type="danger" @click="handleEmptyRecycle">清空回收站</el-button>
          </div>
        </template>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts" name="tabs">
import { ref, reactive, onMounted } from 'vue'; // 导入 onMounted
import { ElMessage } from 'element-plus';
import service from '../utils/request'; // 导入 service

const message = ref('first');
const state = reactive({
  unread: [],
  read: [],
  recycle: []
});

// 1. 从后端获取所有数据
const getData = async () => {
  try {
    const res = await service.get('/message/all');
    if (res.code === '200') {
      state.unread = res.data.unread || [];
      state.read = res.data.read || [];
      state.recycle = res.data.recycle || [];
    } else {
      ElMessage.error(res.message || '加载消息失败');
    }
  } catch (error) {
    ElMessage.error('连接后端失败');
  }
};

// 在组件挂载时自动加载数据
onMounted(() => {
  getData();
});

// 2. 标为已读 (0 -> 1)
const handleRead = async (id: number, index: number) => {
  try {
    const res = await service.put(`/message/read/${id}`);
    if (res.code === '200') {
      const item = state.unread.splice(index, 1);
      state.read = item.concat(state.read);
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

// 3. 删除到回收站 (1 -> 2)
const handleDel = async (id: number, index: number) => {
  try {
    const res = await service.put(`/message/recycle/${id}`);
    if (res.code === '200') {
      const item = state.read.splice(index, 1);
      state.recycle = item.concat(state.recycle);
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

// 4. 从回收站还原 (2 -> 1)
const handleRestore = async (id: number, index: number) => {
  try {
    const res = await service.put(`/message/restore/${id}`);
    if (res.code === '200') {
      const item = state.recycle.splice(index, 1);
      state.read = item.concat(state.read);
    } else {
      ElMessage.error(res.message || '还原失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

// 5. 全部标为已读 (0 -> 1)
const handleReadAll = async () => {
  try {
    const res = await service.put('/message/read-all');
    if (res.code === '200') {
      ElMessage.success('全部已读');
      getData(); // 重新加载数据
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

// 6. 删除全部已读 (1 -> 2)
const handleRecycleAllRead = async () => {
  try {
    const res = await service.put('/message/recycle-all-read');
    if (res.code === '200') {
      ElMessage.success('删除全部');
      getData(); // 重新加载数据
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

// 7. 清空回收站 (DELETE status 2)
const handleEmptyRecycle = async () => {
  try {
    const res = await service.delete('/message/recycle-bin');
    if (res.code === '200') {
      ElMessage.success('已清空');
      getData(); // 重新加载数据
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  }
};

</script>

<style>
.message-title {
  cursor: pointer;
}

.handle-row {
  margin-top: 30px;
}
</style>