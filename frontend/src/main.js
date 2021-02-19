import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import routes from './routes'
import store from './vuex/store'
import vuetify from './plugins/vuetify'
import './plugins/firebase'
import VueGeolocation from 'vue-browser-geolocation';
import firebase from "firebase/app";
import "firebase/messaging";



Vue.config.productionTip = false

Vue.use(VueRouter)
Vue.use(VueGeolocation);
// TODO: beforeEnter로 토큰이 없으면 들어 갈 수 없는 페이지 구성

const router = new VueRouter({
    routes,
    mode: 'history',
});

router.beforeEach((to, from, next) => {
    firebase.auth().onAuthStateChanged(function(user) {
      if (to.name === 'Login' || to.name === 'ConfirmEmail' || to.name === 'Join'|| to.name === 'ActionUrlHandler') { 
        next()
      } else if (user) {
        if (to.meta.title) {
          document.title = to.meta.title;
        } 
        next();
      } else {
        sessionStorage.removeItem('uid')
        sessionStorage.removeItem('jwt')
        next('/')
      }
    }
  )
});

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app');

const messaging = firebase.messaging();
messaging.onMessage((payload) => {
  console.log('Message received. ', payload);
});