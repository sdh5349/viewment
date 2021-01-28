<template>
  <div id="app">
    <v-app id="inspire">
      <!-- <v-card class="overflow-hidden"> -->
        <v-app-bar
          fixed
          color="white"
          elevate-on-scroll
        >
          <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
          <v-btn icon>
            <v-icon
              @click="goPrevious"
            >
              mdi-arrow-left
            </v-icon>
          </v-btn>

          <v-toolbar-title>{{$route.meta.title}}</v-toolbar-title>
        
          <v-spacer></v-spacer>
    
          <v-btn 
            icon
            @click="goSearch"
          >
            <v-icon>
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
            <router-view
             class="mt-5"
             @login="login=true"
            />
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
export default {
  name: 'App',
  components: {
  },
  data: () => ({
    login: false,
  }),
  methods: {
    // onLogout() {
    //   sessionStorage.removeItem('jwt')
    //   this.login = false
    //   this.$router.push({ name: 'Login' })
    // },
    onChangePassword() {
      this.$router.push({ name: 'ChangePassword' })
      .catch (err=>{})
    },
    goPrevious() {
      this.$router.go(-1)
    },
    createArticle() {
      this.$router.push({ name: '' })
      .catch (err=>{})
    },
    goSearch() {
      this.$router.push({ name: 'Search' })
      .catch (err=>{})
    },
    goFeed() {
      const token = sessionStorage.getItem('jwt')
      if (token) {
        // this.pageName = this.$router.meta.title
        this.$router.push({ name: 'FeedMain' })
        .catch (err=>{})
      }
      else{
        alert("login required")
      }
    },
    goAlarm() {
      this.$router.push({ name: '' })
      .catch (err=>{})
    },
    goMessage() {
      this.$router.push({ name: '' })
      .catch (err=>{})
    },
    goProfile() {
      const loginUserId = sessionStorage.getItem('uid')
      this.$router.push({ 
        name: 'Profile', 
        params: { profileUserId : loginUserId }
      })
      .catch (err=>{})
    },
  },
  created() {
    const token = sessionStorage.getItem('jwt')
    if (token) {
      this.login = true
    }
  },
};
</script>

<style>

</style>