

import Login from './views/user/Login.vue'
import Join from './views/user/Join.vue'
import ConfirmEmail from './views/user/ConfirmEmail.vue'
import Error from './views/error/Error.vue'
import NotFound from './views/error/NotFound.vue'
import ChangePassword from './views/user/ChangePassword.vue'
import FeedMain from './views/feed/IndexFeed.vue'
import ActionUrlHandler from './views/user/ActionUrlHandler.vue'
import Search from './views/search/Search.vue'


export default [
    {
        path : '/',
        name : 'Login',
        meta : {title: '로그인'},
        component : Login
    },
    {
        path : '/user/join/',
        name : 'Join',
        meta : {title: '회원가입'},
        component : Join,
    },
    {
        path : '/feed/main',
        name : 'FeedMain',
        meta : {title: '피드'},
        component : FeedMain
    },
    {
        path : '/user/find',
        name : 'ConfirmEmail',
        component : ConfirmEmail
    },
    {
        path : '/user/error',
        name : 'Error',
        component : Error
    },
    {
        path : '/user/error/404',
        name : 'NotFound',
        component : NotFound
    },
    {
        path : '/user/resetPassword',
        name : 'ChangePassword',
        component : ChangePassword
    },
    {
        path : '/user/action-url-handler',
        name : 'ActionUrlHandler',
        component : ActionUrlHandler,
    },
    {
        path: '*',
        redirect: '/user/error/404'
    },
    {
        path : '/search/search',
        name : 'Search',
        meta : { title: '검색'},
        component : Search,
    },
]
