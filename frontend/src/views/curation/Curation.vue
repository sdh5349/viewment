<template>
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular
      indeterminate
      color="primary"
    ></v-progress-circular>
  </v-row>
  <v-row
    v-else
    justify="center"
  >
    <v-col
      class="px-0"
      lg="4"
      md="4"
      sm="6"
    >
    
      <div class="px-3 pt-3">
        <p class="mb-1">현재 위치를 기반으로 인기 장소를 추천해드립니다.</p>
      </div>
   
      <RecommandsByLocation />
      
      <div class="px-3 pt-3">
        <p class="mb-1">회원님의 활동을 기반으로 추천해드립니다.</p>
      </div>

      <Feed feed-type="recommend"/>

    </v-col>
  </v-row>
</template>

<script>
import Feed from '@/components/newsfeed/Feed'
import RecommandsByLocation from '@/components/curation/RecommandsByLocation'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Curation',
  components:{
    Feed,
    RecommandsByLocation
  },
  data() {
    return {
      loading: true,
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
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = false
    },
  },
}
</script>

<style scoped>
/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
  .scroll-container {
    width: 100%;
    overflow: hidden;
    margin-bottom: 50px;
    padding: 0;
  }
</style>