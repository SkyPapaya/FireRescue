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
        <!--        id-->
        <el-table-column
            prop="id"
            label="ID"
            width="55"
            align="center"
        ></el-table-column>
        <!--        用户名-->
        <el-table-column
            prop="name"
            label="用户名"
            align="center"
        ></el-table-column>
        <!--       邮箱 -->
        <el-table-column label="邮箱" prop="e_mail" align="center">
        </el-table-column>
        <!--          电话号码-->
        <el-table-column label="电话号码" prop="phone" align="center">
        </el-table-column>
        <!--        权限-->
        <el-table-column label="权限" prop="authority" align="center">
        </el-table-column>
        <!--        地址-->
        <el-table-column
            prop="address"
            label="地址"
            align="center"
        ></el-table-column>

        <!--        注册时间-->
        <el-table-column
            prop="createdTime"
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

// 模拟API返回数据
const fetchData = async () => {
  // 模拟数据
  return {
    data: {
      list: [
        {
          id: 1,
          name: "John Doe",
          e_mail: "john@example.com",
          phone: "1234567890",
          authority: "用户",
          address: "123 Main St",
          createdTime: "2024-04-05",
        },
        {
          id: 2,
          name: "Jane Smith",
          e_mail: "jane@example.com",
          phone: "0987654321",
          authority: "管理员",
          address: "456 Elm St",
          createdTime: "2024-04-06",
        },

      ],
      pageTotal: 50,
    },
  };
};

interface TableItem {
  id: number;
  name: string;
  e_mail: string;
  phone: string;
  authority: number;
  address: string;
  createdTime: string;
}

const query = reactive({
  address: "",
  name: "",
  pageIndex: 1,
  pageSize: 10,
});

const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);

const getData = async () => {
  const res = await fetchData();
  //tableData.value = res.data.list;
  pageTotal.value = res.data.pageTotal || 50;
};

const handleSearch = () => {
  query.pageIndex = 1;
  getData();
};

const handlePageChange = (val: number) => {
  query.pageIndex = val;
  getData();
};

const visible = ref(false);
let idx: number = -1;
const idEdit = ref(false);
const rowData = ref<TableItem>();
const handleEdit = (index: number, row: TableItem) => {
  idx = index;
  rowData.value = row;
  idEdit.value = true;
  visible.value = true;
};
const updateData = (row: TableItem) => {
  idEdit.value ? (tableData.value[idx] = row) : tableData.value.unshift(row);
  closeDialog();
};

const closeDialog = () => {
  visible.value = false;
  idEdit.value = false;
};

const visible1 = ref(false);
const handleView = (row: TableItem) => {
  rowData.value = row;
  visible1.value = true;
};

const handleAdd = () => {
  visible.value = true;
};

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
