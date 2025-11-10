import { defineStore } from 'pinia';

// (可选) 定义 UserDO 类型，如果需要更严格的类型检查
// interface UserDO {
//     id: number;
//     name: string;
//     authority: string;
//     // ... 其他字段
// }

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            // 1. 从 localStorage 读取 'ms_authority' 作为初始值
            authority: localStorage.getItem('ms_authority') || '',

            // 2. 存储由 router/index.ts 计算出的、该角色可以访问的路由
            accessibleRoutes: [] as any[] // (类型 'any[]' 用于简化，稍后可改为 'RouteRecordRaw[]')
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
            // 注意: ms_keys 已经因为 permiss.ts 被删除而不再使用
        }
    },
    getters: {
        /**
         * 检查当前用户是否拥有指定角色之一
         * (这是一个方便的辅助函数，目前暂未使用，但很有用)
         */
        hasPermission: (state) => (roles: string[]) => {
            if (state.authority) {
                // 检查 state.authority 是否在传入的 roles 数组中
                return roles.includes(state.authority);
            }
            return false;
        }
    }
});