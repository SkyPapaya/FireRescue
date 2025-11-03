<!-- 
消息中心的“全部标为已读”、“全部删除”和“清空回收站”无反应
@member: ljx
-->
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
          <!--ljx:添加@click事件-->
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
            <!--ljx:添加@click事件-->
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
            <!--ljx:添加@click事件-->
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
