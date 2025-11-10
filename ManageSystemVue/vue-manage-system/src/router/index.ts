// 文件路径: src/router/index.ts (恢复后的简单版本)

import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { useUserStore } from '../store/userStore'; // 导入 store
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { publicRoutes, asyncRoutes } from './index-routes';
// (移除了 service 导入)

/**
 * 递归过滤异步路由
 * (函数保持不变) [cite: 558-561]
 */
function filterAsyncRoutes(routes: RouteRecordRaw[], authority: string): RouteRecordRaw[] {
    const accessibleRoutes: RouteRecordRaw[] = [];
    routes.forEach(route => {
        const tmp = { ...route };
        const hasPermission = !tmp.meta || !tmp.meta.roles || (tmp.meta.roles as string[]).includes(authority);

        if (hasPermission) {
            if (tmp.children) {
                tmp.children = filterAsyncRoutes(tmp.children, authority);
            }
            accessibleRoutes.push(tmp);
        }
    });
    return accessibleRoutes;
}


// (createRouter 保持不变) [cite: 561-562]
const router = createRouter({
    history: createWebHashHistory(),
    routes: publicRoutes,
});

// (routesAdded, catchAllRoute 保持不变) [cite: 562]
let routesAdded = false;
const catchAllRoute: RouteRecordRaw = {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/404'
};

/**
 * 核心：全局路由守卫 (恢复简单版本)
 */
router.beforeEach(async (to, from, next) => {
    NProgress.start();

    const userStore = useUserStore();
    const authority = userStore.authority;

    if (authority) {
        // --- 1. 用户已登录 ---
        if (to.path === '/login') {
            next('/');
            NProgress.done();
        } else {
            if (!routesAdded) {
                try {
                    // 1. 根据角色过滤路由
                    const finalRoutes = filterAsyncRoutes(asyncRoutes, authority);

                    // 2. 动态添加过滤后的路由
                    finalRoutes.forEach(route => {
                        router.addRoute(route);
                    });

                    // 3. 添加 404
                    router.addRoute(catchAllRoute);

                    // 4. 将路由列表存入 Pinia (供 sidebar.vue 使用)
                    userStore.setAccessibleRoutes(publicRoutes.concat(finalRoutes));

                    routesAdded = true;
                    next({ ...to, replace: true });

                } catch (error) {
                    console.error('动态添加路由失败:', error);
                    userStore.clearAuth();
                    next('/login');
                    NProgress.done();
                }
            } else {
                next(); // 路由已添加，直接放行
            }
        }
    } else {
        // --- 2. 用户未登录 (保持不变) --- [cite: 572-574]
        if (to.path === '/login' || to.path === '/404' || to.path === '/403') {
            next();
        } else {
            next('/login');
            NProgress.done();
        }
    }
});

router.afterEach(() => {
    NProgress.done();
});

export default router;