// 这是一个新文件，存放路由定义
import { RouteRecordRaw } from 'vue-router';
import Home from '../views/home.vue';

/**
 * 公共路由
 */
export const publicRoutes: RouteRecordRaw[] = [
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
    {
        path: '/404',
        name: '404',
        meta: {
            title: '未找到页面',
        },
        component: () => import(/* webpackChunkName: "404" */ '../views/404.vue'),
    },
    {
        path: '/:pathMatch(.*)*', // 捕获所有未匹配的路由
        name: 'NotFound',
        redirect: '/404'
    },
];

/**
 * 异步路由 (即 权限路由)
 */
export const asyncRoutes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        redirect: '/dashboard',
        meta: {
            title: '系统首页',
            icon: 'Odometer',
            roles: ['admin', 'user']
        },
        children: [
            {
                path: '/dashboard',
                name: 'dashboard',
                meta: {
                    title: '系统首页',
                    icon: 'Odometer',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/dashboard.vue'),
            },
            {
                path: '/management',
                name: 'management',
                meta: {
                    title: '人员及设备管理',
                    icon: 'Calendar',
                    roles: ['admin']
                },
                redirect: '/management/table',
                children: [
                    {
                        path: '/management/table',
                        name: 'basetable',
                        meta: {
                            title: '人员管理',
                            roles: ['admin']
                        },
                        component: () => import(/* webpackChunkName: "table" */ '../views/table.vue'),
                    },
                    {
                        path: '/management/device-management',
                        name: 'deviceManagement',
                        meta: {
                            title: '设备列表管理',
                            roles: ['admin']
                        },
                        component: () => import(/* webpackChunkName: "deviceManagement" */ '../views/deviceManagement.vue'),
                    },
                    {
                        path: '/management/device-monitor',
                        name: 'deviceMonitor',
                        meta: {
                            title: '设备实时监控',
                            roles: ['admin']
                        },
                        redirect: '/management/device-monitor/njupt',
                        children: [
                            {
                                path: '/management/device-monitor/njupt',
                                name: 'device in NUJPT',
                                meta: {
                                    title: '万达茂设备监控',
                                    roles: ['admin'],
                                },
                                component: () => import(/* webpackChunkName: "table" */ '../views/WanDaMao.vue'),
                            },
                            {
                                path: '/management/device-monitor/nju',
                                name: 'device in NJU',
                                meta: {
                                    title: '金鹰设备监控',
                                    roles: ['admin'],
                                },
                                component: () => import(/* webpackChunkName: "table" */ '../views/JingYin.vue'),
                            },
                        ]
                    }
                ]
            },
            {
                path: '/tabs',
                name: 'tabs',
                meta: {
                    title: '消息中心',
                    icon: 'DocumentCopy',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "tabs" */ '../views/tabs.vue'),
            },
            {
                path: '/charts',
                name: 'basecharts',
                meta: {
                    title: '火情数据统计',
                    icon: 'PieChart',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "charts" */ '../views/charts.vue'),
            },
            {
                path: '/monitor',
                name: 'monitor',
                meta: {
                    title: '生命体征监测',
                    icon: 'CoffeeCup',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "donate" */ '../views/monitor.vue'),
            },
            {
                path: '/control',
                name: 'control',
                meta: {
                    title: '设备控制',
                    icon: 'Tools',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "donate" */ '../views/control.vue'),
            },
            {
                path: '/map',
                name: 'map',
                meta: {
                    title: '逃生路线',
                    icon: 'el-icon-picture-outline',
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "dashboard" */ '../views/map.vue'),
            },
            {
                path: '/user',
                name: 'user',
                meta: {
                    title: '个人中心',
                    icon: 'User',
                    hidden: true,
                    roles: ['admin', 'user']
                },
                component: () => import(/* webpackChunkName: "user" */ '../views/user.vue'),
            },
        ],
    },
];