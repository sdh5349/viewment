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
    item-height="500"
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
import FeedItem from './FeedItem.vue'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Feed',
  components: {
    FeedItem,
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
    this.readMore()
  },
  methods: {
    readMore() {
      // 필요한 데이터 가져오기
      console.log(this.centerPosition)
      this.loading = true
      const loginUserId = sessionStorage.getItem('uid')
      // feedType이 recommand 인지 newsfeed인지에 따라 요청 url을 변경한다.
      const url = this.feedType === 'recommend' ? 
      `${SERVER_URL}/recommendations/articles` :  `${SERVER_URL}/articles/feed/${loginUserId}?lat=${this.centerPosition.lat}&lng=${this.centerPosition.lng}`

      axios.get(url, this.getToken)
      .then(res => {
        console.log("url 확인", url, res.data)
        this.feedItems.push(...res.data)
        console.log("피드아이템들",this.feedItems)

        this.page += 1
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
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.readMore()
      }
    },
  }
}
</script>

<style>
</style>