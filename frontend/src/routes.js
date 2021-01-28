

import Login from './views/user/Login.vue'
import Join from './views/user/Join.vue'
import ConfirmEmail from './views/user/ConfirmEmail.vue'
import Error from './views/error/Error.vue'
import NotFound from './views/error/NotFound.vue'
import ChangePassword from './views/user/ChangePassword.vue'
import FeedMain from './views/feed/IndexFeed.vue'
import ActionUrlHandler from './views/user/ActionUrlHandler.vue'
import Profile from './views/user/Profile.vue'
import FollowList from './views/user/FollowList.vue'
import MemoryList from './views/memory/MemoryList.vue'
import EditProfile from './views/user/EditProfile.vue'
import EditAccount from './views/user/EditAccount.vue'


export default [
    {
        path : '/',
        name : 'Login',
        component : Login,
        props : true,
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
        path : '/user/reset-password',
        name : 'ChangePassword',
        component : ChangePassword
    },
    {
        path : '/user/action-url-handler',
        name : 'ActionUrlHandler',
        component : ActionUrlHandler,
    },
    {
        path : '/user/profile/:profileUserId',
        name : 'Profile',
        component : Profile,
        props : true,
    },
    {
        path : '/user/profile/:profileUserId/follow-list',
        name : 'FollowList',
        component : FollowList,
        props : true,
    },
    {
        path : '/memory/:profileUserId/memory-list',
        name : 'MemoryList',
        component : MemoryList,
    },
    {
        path : '/user/profile/edit',
        name : 'EditProfile',
        component : EditProfile,
    },
    {
        path : '/user/account/edit',
        name : 'EditAccount',
        component : EditAccount,
    },
    {
        path: '*',
        redirect: '/user/error/404'
    }
]
