// ==================== 调试专用 ====================
// 此版本会加载所有路由，并完全跳过权限守卫

import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
// 导入我们定义的路由，但暂时不导入 userStore
import { publicRoutes, asyncRoutes } from './index-routes.ts'; // 我们将把路由定义移到另一个文件
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

// 创建 router 实例时，静态加载所有路由
const router = createRouter({
    history: createWebHashHistory(),
    // 【调试改动】：一次性加载所有路由，绕过权限
    routes: publicRoutes.concat(asyncRoutes)
});

// 【调试改动】：完全移除 router.beforeEach 守卫
// (原来的 beforeEach 逻辑被全部注释)

router.afterEach(() => {
    NProgress.done();
});

export default router;
// ==================== 调试专用 ====================