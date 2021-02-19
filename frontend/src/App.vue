<template>
  <div id="app">
    <v-app id="inspire">
      <!-- <v-card class="overflow-hidden"> -->
      <v-app-bar v-if="$route.meta.title != 'Viewment - 로그인'" fixed color="white" elevate-on-scroll>
        <div>
          <v-img src="@/assets/images/viewment.png" height="auto" max-width="150px" width="80%">
          </v-img>
        </div>
        <v-spacer></v-spacer>
        <v-btn
          v-if="$route.meta.title != 'Viewment - 회원가입' && $route.meta.title != 'Viewment - 비밀번호 재설정'"
          icon
          @click="goNotiList"
        >
          <v-badge v-if="uncheckedNoti" bordered dot left color="blue"></v-badge>
          <v-icon>mdi-bell</v-icon>
        </v-btn>
      </v-app-bar>
      <v-app-bar v-else-if="$route.meta.title === 'Viewment - 로그인'" fixed color="white" elevate-on-scroll>
        <div>
          <v-img src="@/assets/images/viewment.png" height="auto" max-width="150px" width="80%">
          </v-img>
        </div>
      </v-app-bar>
      <!-- <v-sheet
          id="scrolling-techniques-7"
          class="overflow-y-auto"
          max-height="600"
        > -->
      <v-container fluid class="mt-5">
        <router-view class="mt-5" @login="loginAndNotiOn()" />
      </v-container>

      <v-bottom-navigation
        grow
        fixed
        v-if="$route.meta.title != 'Viewment - 로그인' && $route.meta.title != 'Viewment - 회원가입'"
      >
        <v-btn icon @click="goCuration">
          <v-icon large>
            mdi-home
          </v-icon>
        </v-btn>

        <v-btn icon @click="goFeed">
          <!-- <span>Nearby</span> -->
          <v-icon large>mdi-map-marker</v-icon>
        </v-btn>

        <v-btn icon @click="goCreateArticle">
          <!-- <span>Favorites</span> -->

          <v-icon large>mdi-plus-box</v-icon>
        </v-btn>

        <v-btn icon @click="goSearch">
          <v-icon style="width: 28px; height: 28px;">
            fas fa-search
          </v-icon>
        </v-btn>
        <v-btn icon @click="goProfile">
          <!-- <span>Nearby</span> -->

          <v-icon large>mdi-account</v-icon>
        </v-btn>
      </v-bottom-navigation>
    </v-app>
  </div>
</template>

<script>
import firebase from 'firebase/app';
export default {
  name: 'App',
  components: {},
  data: () => ({
    login: false,
    notiRef: null,
    uncheckedNoti: false,
  }),
  methods: {
    // onLogout() {
    //   sessionStorage.removeItem('jwt')
    //   this.login = false
    //   this.$router.push({ name: 'Login' })
    // },
    // onChangePassword() {
    //   this.$router.push({ name: 'ChangePassword' })
    //   .catch (err=>{})
    // },
    // goPrevious() {
    //   // this.$router.go(-1)
    //   document.location.replace("");
    // },
    goSearch() {
      this.$router.push({ name: 'Search' }).catch((err) => {
        if (err.name === 'NavigationDuplicated') {
          location.reload();
        }
      });
    },
    goFeed() {
      const token = sessionStorage.getItem('jwt');
      if (token) {
        this.$router.push({ name: 'NewsFeed' }).catch((err) => {
          if (err.name === 'NavigationDuplicated') {
            location.reload();
          }
        });
      } else {
        alert('login required');
      }
    },
    goNotiList() {
      this.$router.push({ name: 'NotiList' }).catch((err) => {
        if (err.name === 'NavigationDuplicated') {
          location.reload();
        }
      });
      this.uncheckedNoti = false;
    },
    goCuration() {
      this.$router.push({ name: 'Curation' }).catch((err) => {
        if (err.name === 'NavigationDuplicated') {
          location.reload();
        }
      });
    },
    goProfile() {
      const loginUserId = sessionStorage.getItem('uid');
      this.$router
        .push({
          name: 'Profile',
          params: { profileUserId: loginUserId },
        })
        .catch((err) => {
          if (err.name === 'NavigationDuplicated') {
            location.reload();
          }
        });
    },
    goCreateArticle() {
      this.$router.push({ name: 'CreateArticle' }).catch((err) => {
        if (err.name === 'NavigationDuplicated') {
          location.reload();
        }
      });
    },
    loginAndNotiOn() {
      this.login = true;
      this.notiOn();
    },
    notiOn() {
      if (this.login) {
        this.notiRef = firebase.database().ref('noti/' + sessionStorage.getItem('uid'));
        this.notiRef.on('value', (snapshot) => {
          this.uncheckedNoti = true;
        });
      }
    },
  },
  created() {
    const token = sessionStorage.getItem('jwt');
    if (token) {
      this.login = true;
    }
    this.notiOn();
  },
};
</script>

<style scoped>
/* 컨테이너의 높이를 화면에 꽉차게 늘린다 */
.stretch-height {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
