<template>
  <div class="sidebar" style="background-color: #99CCFF">
    <el-menu
        class="sidebar-el-menu"
        :default-active="onRoutes"
        :collapse="sidebar.collapse"
        background-color="#324157"
        text-color="#bfcbd9"
        active-text-color="#20a0ff"
        unique-opened
        router
    >
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-sub-menu
              :index="item.index"
              :key="item.index"
              v-permiss="item.permiss"
          >
            <template #title>
              <el-icon>
                <component :is="item.icon"></component>
              </el-icon>
              <span>{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-sub-menu
                  v-if="subItem.subs"
                  :index="subItem.index"
                  :key="subItem.index"
                  v-permiss="item.permiss"
              >
                <template #title>{{ subItem.title }}</template>
                <el-menu-item
                    v-for="(threeItem, i) in subItem.subs"
                    :key="i"
                    :index="threeItem.index"
                >
                  {{ threeItem.title }}
                </el-menu-item>
              </el-sub-menu>
              <el-menu-item
                  v-else
                  :index="subItem.index"
                  v-permiss="item.permiss"
              >
                {{ subItem.title }}
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
        <template v-else>
          <el-menu-item
              :index="item.index"
              :key="item.index"
              v-permiss="item.permiss"
          >
            <el-icon>
              <component :is="item.icon"></component>
            </el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import {computed} from "vue";
import {useSidebarStore} from "../store/sidebar";
import {useRoute} from "vue-router";

const items = [
  {
    icon: "Odometer",
    index: "/dashboard",
    title: "系统首页",
    permiss: "1",
  },
  {
    icon: "Calendar",
    index: "1",
    title: "人员及设备情况管理",
    permiss: "2",
    subs: [
      {
        index: "/table",
        title: "人员管理",
        permiss: "2",
      },
      {
        index: "/device-management",
        title: "设备列表管理",
        permiss: "2",
      },
      {
        index: "/device",
        title: "设备实时监控",
        permiss: "2",
        subs: [
          {
            index: "/NJUPT",
            title: "万达茂",
            permiss: "3",

          },
          {
            index: "/NJU",
            title: "金鹰",
            permiss: "3",

          },

        ]
      },
    ],
  },
  {
    icon: "DocumentCopy",
    index: "/tabs",
    title: "消息中心",
    permiss: "3",
  },
  {
    icon: "PieChart",
    index: "/charts",
    title: "火情相关数据统计",
    permiss: "11",
  },

  {
    icon: "CoffeeCup",
    index: "/monitor",
    title: "生命体征监测",
    permiss: "14",

  },
  {
    icon: "Tools",
    index: "/control",
    title: "设备控制",
    permiss: "15",
  },
  {
    icon:"el-icon-picture-outline",
    index: "/map",
    title: "逃生路线",
    permiss: "16",
  }
];

const route = useRoute();
const onRoutes = computed(() => {
  return route.path;
});

const sidebar = useSidebarStore();
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}

.sidebar > ul {
  height: 100%;
}
</style>
