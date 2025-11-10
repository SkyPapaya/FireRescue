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
      <template v-for="item in sidebarItems">
        <template v-if="item.children && item.children.length > 0">

          <el-sub-menu
              :index="item.path"
              :key="item.path"
          >
            <template #title>
              <el-icon>
                <component :is="item.meta.icon"></component>
              </el-icon>
              <span>{{ item.meta.title }}</span>
            </template>

            <template v-for="subItem in item.children">

              <el-sub-menu
                  v-if="subItem.children && subItem.children.length > 0"
                  :index="subItem.path"
                  :key="subItem.path"
              >
                <template #title>
                  <span>{{ subItem.meta.title }}</span>
                </template>

                <el-menu-item
                    v-for="(threeItem, i) in subItem.children"
                    :key="i"
                    :index="threeItem.path"
                >
                  <span>{{ threeItem.meta.title }}</span>
                </el-menu-item>

              </el-sub-menu>

              <el-menu-item
                  v-else
                  :index="subItem.path"
                  :key="subItem.path + '-item'"
              >
                <span>{{ subItem.meta.title }}</span>
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>

        <template v-else>
          <el-menu-item
              :index="item.path"
              :key="item.path + '-main'"
          >
            <el-icon>
              <component :is="item.meta.icon"></component>
            </el-icon>
            <template #title>{{ item.meta.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useSidebarStore } from "../store/sidebar";
import { useRoute }from "vue-router";
// 1. 导入 userStore
import { useUserStore } from "../store/userStore";

// 2. ⬇️ ⬇️ ⬇️ 删除静态的 'items' 数组 ⬇️ ⬇️ ⬇️
// const items = [ ... (整个数组被删除) ... ];
// ⬆️ ⬆️ ⬆️

const route = useRoute();
const onRoutes = computed(() => {
  return route.path;
});

const sidebar = useSidebarStore();
// 3. 获取 userStore 实例
const userStore = useUserStore();

// 4. 【核心改动】从 userStore 动态计算菜单项
const sidebarItems = computed(() => {
  // 'accessibleRoutes' 包含了所有路由
  // 我们只关心 'Home' 路由 (path: '/') 下的 children
  const homeRoute = userStore.accessibleRoutes.find(r => r.name === 'Home');

  if (homeRoute && homeRoute.children) {
    // 过滤掉 meta.hidden = true 的路由 (如果需要的话)
    return homeRoute.children.filter(
        route => !route.meta || !route.meta.hidden
    );
  }
  return []; // 返回空数组
});

</script>

<style scoped>
/* (样式保持不变) */
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