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
  <v-virtual-scroll
    v-else
    :items="feedItems"
    class="scroll-container"
    @scroll.native="scrolling"
  >
    <template v-slot:default="{ item }">
      <FeedItem :article-info="item"/>
    </template>
  </v-virtual-scroll>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Feed',
  props: {
    feedType: String,
  },
  data() {
    return {
      loading: true,
      feedItems: null,
      page: 0,
      size: 10,
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
    // this.readMore
  },
  methods: {
    readMore() {
      // 필요한 데이터 가져오기
      this.loading = true
      if (this.feedType === 'recommand') {
        // 사용자 좋아요 기반 추천 게시물에 대한 데이터를 가져온다
        axios.get(`${SERVER_URL}?page=${this.page}&size=${this.size}`, this.getToken)
        .then(res => {
          this.feedItems.push(...res.data.content)
          this.page += 1
          this.last = res.data.last
        })
        .then(() => {
          this.loading = false
        })
        .catch(err => {

        })
      } else {
        // 뉴스피드 피드 게시물에 대한 데이터를 가져온다
        axios.get(`${SERVER_URL}?page=${this.page}&size=${this.size}`, this.getToken)
        .then(res => {
          this.feedItems.push(...res.data.content)
          this.page += 1
          this.last = res.data.last
        })
        .then(() => {
          this.loading = false
        })
        .catch(err => {

        })
      }
    },
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.readMore()
      }
    },
  }
}
</script>

<style>

</style>