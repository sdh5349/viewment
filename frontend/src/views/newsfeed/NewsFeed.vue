<template>
  <v-row
    class="news-feed-container"
    >
      <v-col
        lg="4"
        md="4"
        sm="6"
      >
        <v-row>   
          <v-tabs 
            fixed-tabs
            color="primary"
          >
            <v-col cols='6'>
              <v-tab @click="goMap">
                <v-icon>mdi-map</v-icon>
              </v-tab>
            </v-col>
            
            <v-col cols='6'>
              <v-tab @click="goFeed">
                  <v-icon>mdi-post</v-icon>
              </v-tab>
            </v-col>
          </v-tabs>
        </v-row>

        <v-row class="map-feed-row">

        <v-col>
            <div v-if="view === 0">   
              <Map 
                @onClick="saveMemory"
              />
            </div>
            <div v-if="view === 1">
              <Feed 
                feed-type="newsfeed" 
              />
            </div>
          </v-col>
        </v-row>
   </v-col>
 </v-row>
</template>

<script>
import Map from '@/components/newsfeed/Map.vue'
import Feed from '@/components/newsfeed/Feed.vue'
// import MemoryDrawer from '@/components/newsfeed/MemoryDrawer.vue'
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  components: {
    Map,
    Feed,
    // MemoryDrawer
  },
  data() {
    return {
      view: 0,
      goMemoryInfo: '',
      myMemories: ''
    }
  },
  methods: {
    goMap() {
      this.view = 0
    },
    goFeed() {
      this.view = 1
    },
    saveMemory(res) {
      
      if (res){
        const userId = sessionStorage.getItem('uid')
        axios.post(`${SERVER_URL}/users/${userId}/memories`, res, this.getToken)
        .then(()=> {
          alert('이 장소를 기억했습니다.')
          this.$router.go()
        })
        .catch((err)=> {
          console.log(err)
        })
      }
      else {
        alert('기억할 위치를 설정 안했습니다.')
      }
    },
    goToMemory(res) {
      this.goMemoryInfo = res
    },
    getMemories(res) {
      this.myMemories = res
    },
    getArticles() {

    }
  },
  computed: {
    getToken(){
      const token = sessionStorage.getItem('jwt')
      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
}
</script>

<style scoped>
.news-feed-container{
   display: flex; 
   justify-content: center; 
   align-items: center;
}
.map-feed-row {
  position: relative;
  display: flex; 
  justify-content: center; 
  align-items: center;
  height: 100%;
}
.map-drawer-row {
  position: relative;
  display: flex; 
  justify-content: center; 
  align-items: center;
}

</style>