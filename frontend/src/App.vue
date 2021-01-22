<template>
  <div id="app">
    <v-app id="inspire">
      <!-- <v-card class="overflow-hidden"> -->
        <v-app-bar
          fixed
          color="white"
          elevate-on-scroll
          scroll-target="#scrolling-techniques-7"
        >
          <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
          <v-btn icon>
            <v-icon
              @click="goPrevious"
            >
              mdi-arrow-left
            </v-icon>
          </v-btn>
    
          <v-toolbar-title>Title</v-toolbar-title>
    
          <v-spacer></v-spacer>
    
          <v-btn icon>
            <v-icon
              @click="goSearch"
            >
              fas fa-search
            </v-icon>
          </v-btn>
    
          <v-btn icon>
            <v-icon>fas fa-edit</v-icon>
          </v-btn>
    
          <v-btn icon>
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </v-app-bar>
        <!-- <v-sheet
          id="scrolling-techniques-7"
          class="overflow-y-auto"
          max-height="600"
        > -->
          <v-container fluid style="height: 1500px;" class="mt-5">
            <router-view @login="login=true"/>
          </v-container>
        <!-- </v-sheet> -->
      <!-- </v-card> -->
       <v-bottom-navigation fixed>
        <v-btn
          @click="goFeed"
        >
          <v-icon>
            mdi-home
          </v-icon>
        </v-btn>  

        <v-btn
          @click="goAlarm"
        >
          <!-- <span>Favorites</span> -->
    
          <v-icon>mdi-bell</v-icon>
        </v-btn>
    
        <v-btn
          @click="goMessage"
        >
          <!-- <span>Nearby</span> -->
    
          <v-icon>mdi-chat</v-icon>
        </v-btn>

        <v-btn
          @click="goProfile"
        >
          <!-- <span>Nearby</span> -->
    
          <v-icon>mdi-account</v-icon>
        </v-btn>
      </v-bottom-navigation>
    </v-app>
  </div>
  
</template>

<script>
import firebase from 'firebase/app';
export default {
  name: 'App',
  components: {
  },
  data: () => ({
    login: false,
    // pageName: "",
    // tempPageName: "",
    // strPageName: "",
  }),
  methods: {
    // onLogout() {
    //   sessionStorage.removeItem('jwt')
    //   this.login = false
    //   this.$router.push({ name: 'Login' })
    // },
    // onChangePassword() {
    //   this.$router.push({ name: 'ChangePassword' })
    // },
    goPrevious() {
      this.$router.go(-1)
    },
    createArticle() {
      this.$router.push({ name: '' })
    },
    goSearch () {
      this.$router.push({ name: '' })
    },
    // getPageName(pageName,tempPageName,strPageName) {
    //   this.tempPageName = window.location.href
    //   this.strPageName = tempPageName.split("/")
    //   this.pageName = strPageName[strPageName.length-1].split("?")[0]
    //   // return pageName;
    // }
    goFeed() {
      this.$router.push({ name: '' })
    },
    goAlarm() {
      this.$router.push({ name: '' })
    },
    goMessage() {
      this.$router.push({ name: '' })
    },
    goProfile() {
      this.$router.push({ name: '' })
    },
  },
  created() {
    const token = sessionStorage.getItem('jwt')
    if (token) {
      this.login = true
    }
  }
};
</script>

<style>

</style>