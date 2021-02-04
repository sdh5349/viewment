import Login from './views/user/Login.vue'
import Join from './views/user/Join.vue'
import ConfirmEmail from './views/user/ConfirmEmail.vue'
import Error from './views/error/Error.vue'
import NotFound from './views/error/NotFound.vue'
import ChangePassword from './views/user/ChangePassword.vue'
import Feed from './views/feed/Feed.vue'
import ActionUrlHandler from './views/user/ActionUrlHandler.vue'
import Search from './views/search/Search.vue'
import Profile from './views/user/Profile.vue'
import FollowList from './views/user/FollowList.vue'
import MemoryList from './views/memory/MemoryList.vue'
import EditProfile from './views/user/EditProfile.vue'
import EditAccount from './views/user/EditAccount.vue'
import CreateArticle from './views/article/CreateArticle.vue'
import DetailArticle from './views/article/DetailArticle.vue'
import UpdateArticle from './views/article/UpdateArticle.vue'


export default [
    {
        path : '/',
        name : 'Login',
        component : Login,
        props : true,
        meta : {title: '로그인'},
    },
    {
        path : '/user/join/',
        name : 'Join',
        meta : {title: '회원가입'},
        component : Join,
    },
    {
        path : '/feed/main',
        name : 'Feed',
        meta : {title: '피드'},
        component : Feed
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
        path : '/user/:profileUserId',
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
        props : true,
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
    },
    {
        path : '/search/search',
        name : 'Search',
        meta : { title: '검색'},
        component : Search,
    },
    {
        path : '/article/create',
        name : 'CreateArticle',
        component : CreateArticle,
    }, 
    {
        path : '/article/:articleId',
        name : 'DetailArticle',
        component : DetailArticle,
        props: true,
    }, 
    {
        path : '/article/update/:articleId',
        name : 'UpdateArticle',
        component : UpdateArticle,
    }, 
]
