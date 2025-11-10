// 文件路径: src/router/index.ts (最终的生产版本)

import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { useUserStore } from '../store/userStore'; // 导入 store
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

// 1. 从我们分离的路由文件中导入路由
import { publicRoutes, asyncRoutes } from './index-routes';

/**
 * 递归过滤异步路由
 * @param routes 待过滤的路由 (asyncRoutes)
 * @param authority 用户的角色 (例如 'admin')
 * @returns 该角色可以访问的路由
 */
function filterAsyncRoutes(routes: RouteRecordRaw[], authority: string): RouteRecordRaw[] {
    const accessibleRoutes: RouteRecordRaw[] = [];
    routes.forEach(route => {
        // 复制一份，避免污染
        const tmp = { ...route };

        // 检查 meta.roles 是否定义，以及 authority 是否在 roles 列表中
        // 如果没有 meta.roles，则默认允许访问
        const hasPermission = !tmp.meta || !tmp.meta.roles || (tmp.meta.roles as string[]).includes(authority);

        if (hasPermission) {
            if (tmp.children) {
                // 如果有子路由，递归过滤
                tmp.children = filterAsyncRoutes(tmp.children, authority);
            }
            accessibleRoutes.push(tmp);
        }
    });
    return accessibleRoutes;
}


// 2. 创建 router 实例时，只加载公共路由
const router = createRouter({
    history: createWebHashHistory(),
    routes: publicRoutes, // 初始只加载公共路由
});

// 3. 标志位，防止路由重复添加
let routesAdded = false;


const catchAllRoute: RouteRecordRaw = {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/404'
};


/**
 * 4. 核心：全局路由守卫
 */
router.beforeEach(async (to, from, next) => {
    NProgress.start();

    // 必须在守卫内部获取 store 实例
    const userStore = useUserStore();
    const authority = userStore.authority; // (等同于 localStorage.getItem('ms_authority'))

    if (authority) {
        // --- 1. 用户已登录 ---
        if (to.path === '/login') {
            // 如果已登录，访问 /login 则重定向到首页
            next('/');
            NProgress.done();
        } else {
            // 检查路由是否已动态添加
            if (!routesAdded) {
                try {
                    // 1. 根据角色过滤路由
                    const finalRoutes = filterAsyncRoutes(asyncRoutes, authority);

                    // 2. 动态添加过滤后的路由
                    finalRoutes.forEach(route => {
                        router.addRoute(route);
                    });

                    router.addRoute(catchAllRoute);

                    // 3. 将完整路由列表存入 Pinia (供 sidebar.vue 使用)
                    userStore.setAccessibleRoutes(publicRoutes.concat(finalRoutes));

                    // 4. 设置标志位
                    routesAdded = true;

                    // 5. 使用 ...to, replace: true 确保路由已完全加载
                    next({ ...to, replace: true });

                } catch (error) {
                    console.error('动态添加路由失败:', error);
                    // 添加失败，清除登录状态并重定向到登录页
                    userStore.clearAuth();
                    next('/login');
                    NProgress.done();
                }
            } else {
                // 路由已添加，直接放行
                next();
            }
        }
    } else {
        // --- 2. 用户未登录 ---
        if (to.path === '/login' || to.path === '/404' || to.path === '/403') {
            // 访问登录页或错误页，放行
            next();
        } else {
            // 其他页面，全部重定向到登录页
            next('/login');
            NProgress.done();
        }
    }
});

router.afterEach(() => {
    NProgress.done();
});

// (注意: header.vue 中的 window.location.reload() 会自动重置 routesAdded 标志位)

export default router;