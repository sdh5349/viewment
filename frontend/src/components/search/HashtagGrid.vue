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
  >
    <v-virtual-scroll
      :items="hashArticles"
      :item-height="60"
      @scroll="scrolling"
      
    >
    <v-col
      :key="item.articleId"
      class="d-flex child-flex "
      cols="4"
    >
    <template v-slot:default="{ item }">    
      <!-- v-for="item in items" -->

        <v-scale-transition>
          <v-img
            :src="imageServerPrefix + item.thumbnail.path"
            :lazy-src="imageServerPrefix + item.thumbnail.path"
            aspect-ratio="1"
            class="grey lighten-2 image-hover grid-item-padding"
            @click="goDetail(item.articleId)"
          >
        </v-img>

      </v-scale-transition>
    
        </template>
        </v-col>
    </v-virtual-scroll>
  </v-row>
</template>


<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data () {
    return {
      hashArticles: [],
      imageServerPrefix: `${SERVER_URL}/images/`,
      loading: true,
      page: 0,
      size: 20,
      last: false,
      first: true,
    }
  },
  props: {
    hash: {
      type: String,
    }
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  methods: {
    fetchData() {
      this.loading = true
      var params = {page:this.page, size:this.size,}
      axios.get(`${SERVER_URL}/articles/searchbyhashtag/${this.hash}/pg`,{params:params,headers:this.getToken.headers})
      .then((res) => {
        this.hashArticles.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
      })
      .then(() => {
        this.loading = false
      })      
      .catch((err) => {
        alert('error' + err.message)
      })
    },
    goDetail(res) {
      this.$router.push({name: 'DetailArticle', params: {articleId :res,}})
    },
    // 스크롤이 맨 아래에 있고 더 요청할 유저의 정보가 남아있다면 팔로워 정보를 더 요청한다
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.fetchData()
      }
    },
  },
  created() {
    this.fetchData()
  },
  watch: {
    '$route': 'fetchData'
  }
}
</script>

<style scoped>
/* 그리드 각 아이템 패딩 설정 */
.grid-item-padding {
  /* padding: 0.3rem; */
  padding-top: 70px;
}
</style>