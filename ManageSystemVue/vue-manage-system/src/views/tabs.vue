<!-- 
消息中心的“全部标为已读”、“全部删除”和“清空回收站”无反应
@member: ljx
-->
<!--
ljx:下面的代码用于备份
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
          <el-table-column prop="date" width="180"></el-table-column>
          <el-table-column width="120">
            <template #default="scope">
              <el-button size="small" @click="handleRead(scope.$index)">标为已读</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="handle-row">
          <!--ljx:添加@click事件--
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
            <el-table-column prop="date" width="180"></el-table-column>
            <el-table-column width="120">
              <template #default="scope">
                <el-button type="danger" size="small" @click="handleDel(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="handle-row">
            <!--ljx:添加@click事件--
            <el-button type="danger" @click="handleDelAll">删除全部</el-button>
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
            <el-table-column prop="date" width="180"></el-table-column>
            <el-table-column width="120">
              <template #default="scope">
                <el-button size="small" @click="handleRestore(scope.$index)">还原</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="handle-row">
            <!--ljx:添加@click事件--
            <el-button type="danger" @click="handleClearAll">清空回收站</el-button>
          </div>
        </template>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts" name="tabs">
import {ref, reactive} from 'vue';

const message = ref('first');
const state = reactive({
  unread: [
    {
      date: '2018-04-19 20:00:00',
      title: '江苏南京一小区发生重大火情'
    },
    {
      date: '2018-04-19 21:00:00',
      title: '南京xx小区大火已经熄灭'
    }
  ],
  read: [
    {
      date: '2018-04-19 20:00:00',
      title: '【系统通知】该系统将于今晚凌晨2点到5点进行升级维护'
    }
  ],
  recycle: [
    {
      date: '2018-04-19 20:00:00',
      title: '【系统通知】该系统将于今晚凌晨2点到5点进行升级维护'
    }
  ]
});

const handleRead = (index: number) => {
  const item = state.unread.splice(index, 1);
  state.read = item.concat(state.read);
};
const handleDel = (index: number) => {
  const item = state.read.splice(index, 1);
  state.recycle = item.concat(state.recycle);
};
const handleRestore = (index: number) => {
  const item = state.recycle.splice(index, 1);
  state.read = item.concat(state.read);
};
// ljx:添加全部标为已读、删除全部以及清空回收站的处理函数
// 全部标为已读
const handleReadAll = () => {
    // 把所有 unread 数组的元素都移动到 read 数组
    state.read = [...state.unread, ...state.read];
    state.unread = [];
};

// 删除全部（已读）
const handleDelAll = () => {
    // 把所有 read 数组的元素都移动到 recycle 数组
    state.recycle = [...state.read, ...state.recycle];
    state.read = [];
};

// 清空回收站
const handleClearAll = () => {
    state.recycle = [];
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
-->
<template>
  <div class="container">
    <el-tabs v-model="message" v-loading="loading" @tab-change="onTabChange">
      <el-tab-pane :label="`未读消息(${unread.length})`" name="first">
        <el-table :data="unread" :show-header="false" style="width: 100%">
          <el-table-column>
            <template #default="scope">
              <span class="message-title">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="messageTime" width="180"></el-table-column>
          <el-table-column width="120">
            <template #default="scope">
              <el-button size="small" @click="handleRead(scope.row)">标为已读</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="handle-row">
          <el-button type="primary" @click="handleReadAll">全部标为已读</el-button>
        </div>
      </el-tab-pane>
      
      <el-tab-pane :label="`已读消息(${read.length})`" name="second">
        <el-table :data="read" :show-header="false" style="width: 100%">
          <el-table-column>
            <template #default="scope">
              <span class="message-title">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="messageTime" width="180"></el-table-column>
          <el-table-column width="120">
            <template #default="scope">
              <el-button type="danger" size="small" @click="handleDel(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="handle-row">
          <el-button type="danger" @click="handleDelAll">删除全部</el-button>
        </div>
      </el-tab-pane>

      <el-tab-pane :label="`回收站(${recycle.length})`" name="third">
        <el-table :data="recycle" :show-header="false" style="width: 100%">
          <el-table-column>
            <template #default="scope">
              <span class="message-title">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="messageTime" width="180"></el-table-column>
          <el-table-column width="120">
            <template #default="scope">
              <el-button size="small" @click="handleRestore(scope.row)">还原</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="handle-row">
          <el-button type="danger" @click="handleClearAll">清空回收站</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts" name="tabs">
import { ref, onMounted } from 'vue';
import service from '../utils/request';
import { ElMessage } from 'element-plus';

const message = ref('first'); // 当前激活的 tab
const loading = ref(false); // 加载状态

// 三个列表的数据源
const unread = ref([]);
const read = ref([]);
const recycle = ref([]);

// 状态码
const STATUS_UNREAD = 0;
const STATUS_READ = 1;
const STATUS_RECYCLE = 2;

// 从后端加载指定状态的消息
const loadMessages = async (status, listRef) => {
  loading.value = true;
  try {
    const res = await service.get(`/message/list/${status}`);
    if (res.code === '200') {
      listRef.value = res.data;
    } else {
      ElMessage.error(res.message || '加载失败');
    }
  } catch (e) {
    ElMessage.error('网络请求失败');
  } finally {
    loading.value = false;
  }
};

// 组件加载时，加载所有列表
onMounted(() => {
  loadMessages(STATUS_UNREAD, unread);
  loadMessages(STATUS_READ, read);
  loadMessages(STATUS_RECYCLE, recycle);
});

// 切换 tab 时，刷新该 tab 的数据
const onTabChange = (tabName) => {
  switch(tabName) {
    case 'first':
      loadMessages(STATUS_UNREAD, unread);
      break;
    case 'second':
      loadMessages(STATUS_READ, read);
      break;
    case 'third':
      loadMessages(STATUS_RECYCLE, recycle);
      break;
  }
};

// 通用的状态更新函数
const updateStatus = async (id, newStatus) => {
  try {
    await service.put(`/message/status/${id}`, { status: newStatus });
  } catch (e) {
    ElMessage.error('操作失败');
  }
};

// 标为已读
const handleRead = async (row) => {
  await updateStatus(row.id, STATUS_READ);
  // 重新加载 "未读" 和 "已读" 列表
  loadMessages(STATUS_UNREAD, unread);
  loadMessages(STATUS_READ, read);
};

// 删除 (移入回收站)
const handleDel = async (row) => {
  await updateStatus(row.id, STATUS_RECYCLE);
  // 重新加载 "已读" 和 "回收站" 列表
  loadMessages(STATUS_READ, read);
  loadMessages(STATUS_RECYCLE, recycle);
};

// 还原
const handleRestore = async (row) => {
  await updateStatus(row.id, STATUS_READ);
  // 重新加载 "已读" 和 "回收站" 列表
  loadMessages(STATUS_READ, read);
  loadMessages(STATUS_RECYCLE, recycle);
};

// 批量更新状态
const batchUpdateStatus = async (fromStatus, toStatus) => {
   try {
    await service.put('/message/status/all', { fromStatus, toStatus });
  } catch (e) {
    ElMessage.error('批量操作失败');
  }
};

// 全部标为已读
const handleReadAll = async () => {
  await batchUpdateStatus(STATUS_UNREAD, STATUS_READ);
  loadMessages(STATUS_UNREAD, unread);
  loadMessages(STATUS_READ, read);
};

// 删除全部 (全部已读 -> 回收站)
const handleDelAll = async () => {
  await batchUpdateStatus(STATUS_READ, STATUS_RECYCLE);
  loadMessages(STATUS_READ, read);
  loadMessages(STATUS_RECYCLE, recycle);
};

// 清空回收站
const handleClearAll = async () => {
  try {
    await service.delete(`/message/status/${STATUS_RECYCLE}`);
    loadMessages(STATUS_RECYCLE, recycle);
  } catch (e) {
     ElMessage.error('清空失败');
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

