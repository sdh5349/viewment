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
  <v-alert
    v-else-if="feedItems.length === 0 && feedType === 'recommend'"
    v-html="'다른 회원을 팔로우하거나<br/>마음에 드는 게시글에 좋아요를 눌러 주세요.'"
    class="ma-0 pa-2 text-center"
    type="info"
  >
  </v-alert>
  <v-alert
    v-else-if="feedItems.length === 0 && feedType === 'newsfeed'"
    v-html="'기억하기 장소 주변에 게시물이 없거나</br>기억하는 장소가 없습니다.'"
    class="ma-0 pa-2 text-center"
    type="info"
  >
  </v-alert>
  <v-virtual-scroll
    v-else
    :items="feedItems"
    item-height="500"
    max-height="80vh"
    @scroll.native="scrolling"
  >
    <template v-slot:default="{ item }">
      <ArticleContents :article="item" />
    </template>
  </v-virtual-scroll>
</template>

<script>
import axios from 'axios'
import ArticleContents from '../article/ArticleContents.vue'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Feed',
  components: {
    ArticleContents,
  },
  props: {
    feedType: String,
    centerPosition: Object
  },
  data() {
    return {
      loading: true,
      feedItems: [],
      page: 0,
      size: 2,
      last: false,
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
    this.readMore()
  },
  methods: {
    readMore() {
      // 필요한 데이터 가져오기
      this.loading = true
      const loginUserId = sessionStorage.getItem('uid')
      // feedType이 recommand 인지 newsfeed인지에 따라 요청 url을 변경한다.
      const url = this.feedType === 'recommend' ? 
      `${SERVER_URL}/recommendations/articles?page=${this.page}&size=${this.size}` :  `${SERVER_URL}/articles/feed/${loginUserId}/pg?lat=${this.centerPosition.lat}&lng=${this.centerPosition.lng}&page=${this.page}&size=${this.size}`

      axios.get(url, this.getToken)
      .then(res => {
        this.feedItems.push(...res.data.content)
        this.page += 1
        console.log(res.data)
        this.last = res.data.last
      })
      .then(() => {
        this.loading = false
      })
      .catch(err => {

      })
    },
    scrolling (event) {
      const scrollInfo = event.target

      // console.log(this.last)
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop <= scrollInfo.clientHeight && !this.last) {
        this.readMore()
      }
    },
  }
}
</script>

<style>
</style>