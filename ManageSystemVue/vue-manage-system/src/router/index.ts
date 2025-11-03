import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router';
import {usePermissStore} from '../store/permiss';
import Home from '../views/home.vue';
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/dashboard',
    },
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/dashboard',
                name: 'dashboard',
                meta: {
                    title: '系统首页',
                    permiss: '1',
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/dashboard.vue'),
            },
            {
                path: '/table',
                name: 'basetable',
                meta: {
                    title: '表格',
                    permiss: '2',
                },
                component: () => import(/* webpackChunkName: "table" */ '../views/table.vue'),
            },
            {
                path: '/device-management',
                name: 'deviceManagement',
                meta: {
                    title: '设备管理',
                    permiss: '2', // 假设和'表格'用一样的权限
                },
                component: () => import(/* webpackChunkName: "deviceManagement" */ '../views/deviceManagement.vue'),
            },
            {
                path: '/NJUPT',
                name: 'device in NUJPT',
                meta: {
                    title: '万达茂设备监控',
                    permiss: '2',
                },
                component: () => import(/* webpackChunkName: "table" */ '../views/WanDaMao.vue'),
            },
            {
                path: '/NJU',
                name: 'device in NJU',
                meta: {
                    title: '金鹰设备监控',
                    permiss: '2',
                },
                component: () => import(/* webpackChunkName: "table" */ '../views/JingYin.vue'),
            },
            {
                path: '/charts',
                name: 'basecharts',
                meta: {
                    title: '图表',
                    permiss: '11',
                },
                component: () => import(/* webpackChunkName: "charts" */ '../views/charts.vue'),
            },

            {
                path: '/tabs',
                name: 'tabs',
                meta: {
                    title: 'tab标签',
                    permiss: '3',
                },
                component: () => import(/* webpackChunkName: "tabs" */ '../views/tabs.vue'),
            },
            {
                path: '/monitor',
                name: 'monitor',
                meta: {
                    title: '生命体征监测',
                    permiss: '14',
                },
                component: () => import(/* webpackChunkName: "donate" */ '../views/monitor.vue'),
            },
            {
                path: '/control',
                name: 'control',
                meta: {
                    title: '设备控制',
                    permiss: '15',
                },
                component: () => import(/* webpackChunkName: "donate" */ '../views/control.vue'),

            },
            {
                path: '/map',
                name: 'map',
                meta: {
                    title: '逃生路线',
                    permiss: '1',
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/map.vue'),
            },
            {
                path: '/permission',
                name: 'permission',
                meta: {
                    title: '权限管理',
                    permiss: '13',
                },
                component: () => import(/* webpackChunkName: "permission" */ '../views/permission.vue'),
            },
            {
                path: '/user',
                name: 'user',
                meta: {
                    title: '个人中心',
                },
                component: () => import(/* webpackChunkName: "user" */ '../views/user.vue'),
            },
        ],
    },
    {
        path: '/login',
        name: 'Login',
        meta: {
            title: '登录',
        },
        component: () => import(/* webpackChunkName: "login" */ '../views/login.vue'),
    },
    {
        path: '/403',
        name: '403',
        meta: {
            title: '没有权限',
        },
        component: () => import(/* webpackChunkName: "403" */ '../views/403.vue'),
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    NProgress.start();
    const role = localStorage.getItem('ms_username');
    const permiss = usePermissStore();
    if (!role && to.path !== '/login') {
        next('/login');
    } else if (to.meta.permiss && !permiss.key.includes(to.meta.permiss)) {
        // 如果没有权限，则进入403
        next('/403');
    } else {
        next();
    }
});

router.afterEach(() => {
    NProgress.done()
})

export default router;
