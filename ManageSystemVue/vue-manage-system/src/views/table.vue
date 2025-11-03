<template>
  <div>
    <div class="container">
      <div class="search-box">
        <el-input
            v-model="query.name"
            placeholder="用户名"
            class="search-input mr10"
            clearable
        ></el-input>
        <el-button
            type="warning"
            :icon="CirclePlusFilled"
            @click="handleAdd"
        >新增
        </el-button
        >
      </div>
      <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
      >
        <el-table-column
            prop="id"
            label="ID"
            width="55"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="name"
            label="用户名"
            align="center"
        ></el-table-column>
        <el-table-column label="邮箱" prop="e_mail" align="center">
        </el-table-column>
        <el-table-column label="电话号码" prop="phone" align="center">
        </el-table-column>
        <el-table-column label="权限" prop="authority" align="center">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址"
            align="center"
        ></el-table-column>

        <el-table-column
            prop="gmtCreated"
            label="注册时间"
            align="center"
        ></el-table-column>

        <el-table-column label="操作" width="280" align="center">
          <template #default="scope">
            <el-button
                type="warning"
                size="small"
                :icon="View"
                @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
                type="primary"
                size="small"
                :icon="Edit"
                @click="handleEdit(scope.$index, scope.row)"
                v-permiss="15"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="query.pageSize"
            :total="pageTotal"
            @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

    <el-dialog
        :title="idEdit ? '编辑用户' : '新增用户'"
        v-model="visible"
        width="30%"
        @close="closeDialog"
    >
      <table-edit
          :data="rowData"
          :edit="idEdit"
          :update="updateData"
      ></table-edit>
    </el-dialog>

    <el-dialog title="查看详情" v-model="visible1" width="30%">
      <table-detail :data="rowData"></table-detail>
    </el-dialog>

  </div>
</template>

<script setup lang="ts" name="basetable">
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Delete,
  Edit,
  Search,
  CirclePlusFilled,
  View,
} from "@element-plus/icons-vue";

// 导入我们需要的组件和 service
import TableEdit from '../components/table-edit.vue';
import TableDetail from '../components/table-detail.vue';
import service from '../utils/request';


interface TableItem {
  id: number;
  name: string;
  e_mail: string;
  phone: string;
  authority: number; // 确保类型匹配
  address: string;
  gmtCreated: string; // 确保这个字段名和后端返回的一致
}

const query = reactive({
  address: "",
  name: "",
  pageIndex: 1,
  pageSize: 10,
});

const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);
const visible = ref(false);
let idx: number = -1;
const idEdit = ref(false);
const rowData = ref<TableItem>();
const visible1 = ref(false);


// ========== 这是唯一且正确的 getData 函数 ==========
const getData = async () => {
  try {
    // 请求后端的 /user/userPage 接口，并传入分页参数
    const res = await service.get('/user/userPage', {
      params: {
        pageNum: query.pageIndex,
        size: query.pageSize
      }
    });

    // 你的后端 /userPage 接口返回的数据结构是 { code: "200", data: { total: ..., data: [...] } }
    if (res.code === '200') {
      tableData.value = res.data.data;  // 列表数据
      pageTotal.value = res.data.total; // 总数
    } else {
      ElMessage.error(res.message || '加载数据失败');
    }
  } catch (error) {
    console.error("获取真实数据失败", error);
    ElMessage.error("后端数据加载失败");
  }
};
// ===============================================


const handleSearch = () => {
  query.pageIndex = 1;
  getData();
};
const handlePageChange = (val: number) => {
  query.pageIndex = val;
  getData();
};


const handleEdit = (index: number, row: TableItem) => {
  idx = index;
  rowData.value = { ...row }; // 使用副本以防取消时修改
  idEdit.value = true;
  visible.value = true;
};

// 【已更新】包含 "新增" 和 "编辑" 的数据库逻辑
const updateData = async (row: TableItem) => {
  if (idEdit.value) {
    // 【编辑用户】的逻辑
    try {
      await service.put('/user/updateUser', row);
      ElMessage.success('修改成功');
      getData(); // 重新加载数据
    } catch (error) {
      console.error(error);
      ElMessage.error('修改失败');
    }
  } else {
    // 【新增用户】的逻辑
    try {
      await service.post('/user/insertUser', row);
      ElMessage.success('新增成功');
      getData(); // Geluidsdata
    } catch (error) {
      console.error(error);
      ElMessage.error('新增失败');
    }
  }
  closeDialog();
};

const closeDialog = () => {
  visible.value = false;
  idEdit.value = false;
};


const handleView = (row: TableItem) => {
  rowData.value = row;
  visible1.value = true;
};

// 【已更新】新增时，清空表单
const handleAdd = () => {
  rowData.value = {} as TableItem; // 清空表单
  idEdit.value = false;
  visible.value = true;
};

// 页面加载时获取数据
getData();

</script>

<style scoped>
.search-box {
  margin-bottom: 20px;
}

.search-input {
  width: 200px;
}

.mr10 {
  margin-right: 10px;
}

.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>