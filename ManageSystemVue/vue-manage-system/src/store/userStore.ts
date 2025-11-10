// 文件路径: src/store/userStore.ts
// (请确保文件名和路径正确)

import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            // 1. 从 localStorage 读取 'ms_authority' 作为初始值
            authority: localStorage.getItem('ms_authority') || '',

            // 2. 存储由 router/index.ts 计算出的、该角色可以访问的路由
            accessibleRoutes: [] as any[]
        };
    },
    actions: {
        /**
         * 登录时调用 (由 login.vue 调用)
         * @param auth - 用户角色 (例如 'admin' 或 'user')
         */
        setAuthority(auth: string) {
            this.authority = auth;
            localStorage.setItem('ms_authority', auth);
        },

        /**
         * 动态生成路由时调用 (由 router/index.ts 调用)
         * @param routes - 该角色被允许访问的完整路由列表
         */
        setAccessibleRoutes(routes: any[]) {
            this.accessibleRoutes = routes;
        },

        /**
         * 退出登录时调用 (由 header.vue 调用)
         */
        clearAuth() {
            this.authority = '';
            this.accessibleRoutes = [];
            localStorage.removeItem('ms_authority');
            localStorage.removeItem('ms_username');
        }
    },
    getters: {
        /**
         * 检查当前用户是否拥有指定角色之一
         */
        hasPermission: (state) => (roles: string[]) => {
            if (state.authority) {
                return roles.includes(state.authority);
            }
            return false;
        }
    }
});