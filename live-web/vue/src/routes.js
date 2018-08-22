import Login from './views/Login.vue';
import NotFound from './views/404.vue';
import Home from './views/Home.vue';
import Main from './views/Main.vue';
import content from './views/content/list.vue';
import acct from './views/acct/list.vue';
import catg from './views/catg/list.vue';
import room from './views/room/list.vue';
import Form from './views/content/Form.vue';
import user from './views/content/user.vue';
import echarts from './views/charts/echarts.vue';

let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: Home,
        name: '对话管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/content/list', component: content, name: '内容列表' },
            { path: '/form', component: Form, name: 'Form' },
            { path: '/user', component: user, name: '列表' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '账户管理',
        iconCls: 'el-icon-',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/acct/list', component: acct, name: '账户列表' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: '分类管理',
        iconCls: 'el-icon-',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/catg/list', component: catg, name: '分类列表' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: '房间管理',
        iconCls: 'el-icon-',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/room/list', component: room, name: '房间列表' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: 'Charts',
        iconCls: 'fa fa-bar-chart',
        children: [
            { path: '/echarts', component: echarts, name: 'echarts' }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;