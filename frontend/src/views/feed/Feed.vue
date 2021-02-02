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
      />
    </div>
    <div v-if="view === 1">
      <Feed />
    </div>
</v-container>
</template>

<script>
import Map from '@/components/feed/Map.vue'
import Feed from '@/components/feed/Feed.vue'

export default {
  components: {
    Map,
    Feed
  },
  data() {
    return {
      view: 0,
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
      // 기억하기 요청 보내면 됨
      if (res){
        console.log(res)
      }
      else {
        alert('기억할 위치를 설정 안했습니다.')
      }
    }
  }
}
</script>

<style>

</style>