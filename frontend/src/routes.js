

import Login from './views/user/Login.vue'
import Join from './views/user/join/Join.vue'
import JoinEmailConfirm from './views/user/join/JoinEmailConfirm.vue'
import FindEmailConfirm from './views/user/find/FindEmailConfirm.vue'
import Error from './views/error/Error.vue'
import NotFound from './views/error/NotFound.vue'
import ChangePassword from './views/user/ChangePassword.vue'
import FeedMain from './views/feed/IndexFeed.vue'
import ActionUrlHandler from './views/user/ActionUrlHandler.vue'
import Components from './views/Components.vue'



export default [
    {
        path : '/',
        name : 'Login',
        component : Login
    },
    {
        path : '/user/join/',
        name : 'Join',
        component : Join,
    },
    {
        path : '/feed/main',
        name : 'FeedMain',
        component : FeedMain
    },
    {
        path : '/components',
        name : 'Components',
        component : Components
    },
    {
        path : '/user/join/confirm',
        name : 'JoinEmailConfirm',
        component : JoinEmailConfirm
    },
    {
        path : '/user/find',
        name : 'FindEmailConfirm',
        component : FindEmailConfirm
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
        path : '/user/actionUrlHandler',
        name : 'ActionUrlHandler',
        component : ActionUrlHandler,
    },
    {
        path: '*',
        redirect: '/user/error/404'
    }
]
