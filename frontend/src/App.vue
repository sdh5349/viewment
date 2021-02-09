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
              large
              @click="goPrevious"
            >
              mdi-arrow-left
            </v-icon>
          </v-btn>

          <v-toolbar-title>{{$route.meta.title}}</v-toolbar-title>
        
          <v-spacer></v-spacer>
  
    
          <v-btn 
            icon
            @click="goAlarm"
          >
            <v-icon>mdi-bell</v-icon>
          </v-btn>
  
        </v-app-bar>
        <!-- <v-sheet
          id="scrolling-techniques-7"
          class="overflow-y-auto"
          max-height="600"
        > -->
          <v-container fluid class="mt-5">
            <router-view
            class="mt-5"
            @login="login=true"
            />
          </v-container>
        <!-- </v-sheet> -->
      <!-- </v-card> -->
       <v-bottom-navigation grow fixed>
        <v-btn
          icon
          @click="goCuration"
        >
          <v-icon large>
            mdi-home
          </v-icon>
        </v-btn>  

        <v-btn
          icon
          @click="goFeed"
        >
          <!-- <span>Nearby</span> -->
          <v-icon large>mdi-map-marker</v-icon>
        </v-btn>

        <v-btn
          icon
          @click="goCreateArticle"
        >
          <!-- <span>Favorites</span> -->
    
          <v-icon large>mdi-plus-box</v-icon>
        </v-btn>

        <v-btn 
          icon
          @click="goSearch"
        >
          <v-icon
            style="width: 28px; height: 28px" 
          >
            fas fa-search
          </v-icon>
        </v-btn>
          
        <v-btn
          icon
          @click="goProfile"
        >
          <!-- <span>Nearby</span> -->
    
          <v-icon large>mdi-account</v-icon>
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
    // onChangePassword() {
    //   this.$router.push({ name: 'ChangePassword' })
    //   .catch (err=>{})
    // },
    goPrevious() {
      this.$router.go(-1)
    },
    goSearch() {
      this.$router.push({ name: 'Search' })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    },
    goFeed() {
      const token = sessionStorage.getItem('jwt')
      if (token) {
        this.$router.push({ name: 'NewsFeed' })
        .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
        })
      }
      else{
        alert("login required")
      }
    },
    goAlarm() {
      this.$router.push({ name: '' })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    },
    goCuration() {
      this.$router.push({ name: 'Curation' })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    },
    goProfile() {
      const loginUserId = sessionStorage.getItem('uid')
      this.$router.push({ 
        name: 'Profile', 
        params: { profileUserId : loginUserId }
      })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    },
    goCreateArticle() {
      this.$router.push({ name: 'CreateArticle' })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    }
  },
  created() {
    const token = sessionStorage.getItem('jwt')
    if (token) {
      this.login = true
    }
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