import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import routes from './routes'
import store from './vuex/store'
import vuetify from './plugins/vuetify'
import './plugins/firebase'


Vue.config.productionTip = false

Vue.use(VueRouter)

const router = new VueRouter({
    routes,
    mode: 'history',
});
router.beforeEach((to, from, next) => {
    /* It will change the title when the router is change*/
    if (to.meta.title) {
      document.title = to.meta.title;
    }
    next();
});

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app');
