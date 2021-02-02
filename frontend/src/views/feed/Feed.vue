<template>
  <v-container>
  
  <v-row
    justify="center"
    row wrap
  > 
    <v-tabs
      fixed-tabs
      color="cyan"
    >
      <v-tab
        @click="goMap"
      >
        <v-icon>
          mdi-map
        </v-icon>
      </v-tab>
      <v-tab
        @click="goFeed"
      >
        <v-icon>
          mdi-post
        </v-icon>
      </v-tab>
    </v-tabs>

  </v-row>
    <div v-if="view === 0">
      <Map 
        @onClick="onMemory"
        :goMemoryInfo="goMemoryInfo"
      />
    </div>
    <div v-if="view === 1">
      <Feed />
    </div>


    <MemoryDrawer
      @goToMemory="goToMemory"
    >
    </MemoryDrawer>





</v-container>
</template>

<script>
import Map from '@/components/feed/Map.vue'
import Feed from '@/components/feed/Feed.vue'
import MemoryDrawer from '@/components/feed/MemoryDrawer.vue'
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  components: {
    Map,
    Feed,
    MemoryDrawer
  },
  data() {
    return {
      view: 0,
      goMemoryInfo: '',
    }
  },
  methods: {
    goMap() {
      this.view = 0
    },
    goFeed() {
      this.view = 1
    },
    onMemory(res) {

      if (res){
        const userId = sessionStorage.getItem('uid')
        axios.post(`${SERVER_URL}/users/${userId}/memories`, res, this.getToken)
        .then(()=> {


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

<style>

</style>