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
    class="ma-2 text-center"
    dense
    type="info"
  >
  </v-alert>
  <v-alert
    v-else-if="feedItems.length === 0 && feedType === 'newsfeed'"
    v-html="'기억하기 장소 주변에 게시물이 없거나</br>기억하는 장소가 없습니다.'"
    class="ma-2 text-center"
    dense
    type="info"
  >
  </v-alert>
  <div v-else-if="feedType === 'recommend'" style="margin-bottom: 56px;">
    <div
      v-for="(item, idx) in feedItems"
      :key=idx
      class="mx-0"
    >
      <ArticleContents class="mb-7" :article="item" :is-not-detail="true"/>
    </div>
    <Alert
      v-if="alert.alerted"
      :message="alert.message"
      :color="alert.color ? alert.color : 'error'"
      :is-bottom="true"
    />
  </div>
  <v-container
    v-else
    id="scroll-target"
    class="overflow-y-auto scroll-container pa-0"
  >
    <div
      v-for="(item, idx) in feedItems"
      :key=idx
      v-scroll:#scroll-target="onScroll"
      class="mx-0"
    >
      <ArticleContents class="mb-7" :article="item" :is-not-detail="true"/>
    </div>
    <Alert
      v-if="alert.alerted"
      :message="alert.message"
      :color="alert.color ? alert.color : 'error'"
      :is-bottom="true"
    />
  </v-container>
</template>

<script>
import axios from 'axios'
import ArticleContents from '../article/ArticleContents.vue'
import Alert from '../common/Alert.vue'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Feed',
  components: {
    ArticleContents,
    Alert
  },
  props: {
    feedType: String,
    centerPosition: Object
  },
  data() {
    return {
      loading: true,
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      feedItems: [],
      page: 0,
      size: 5,
      last: false,
      itemHeight: 0,
      loginUserId: sessionStorage.getItem('uid'),
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
  created () {
    window.addEventListener('scroll', this.handleScroll);
    this.readMore()
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    readMore() {
      // 필요한 데이터 가져오기
      // feedType이 recommand 인지 newsfeed인지에 따라 요청 url을 변경한다.
      const url = this.feedType === 'recommend' ? 
      `${SERVER_URL}/recommendations/articles?page=${this.page}&size=${this.size}` :  `${SERVER_URL}/articles/feed/${this.loginUserId}/pg?lat=${this.centerPosition.lat}&lng=${this.centerPosition.lng}&page=${this.page}&size=${this.size}`

      axios.get(url, this.getToken)
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
    },
    onScroll (event) {
      const scrollInfo = event.target
      if (scrollInfo && Math.round(scrollInfo.scrollHeight - scrollInfo.scrollTop) === scrollInfo.clientHeight && !this.last) {
        this.readMore()
      } else if (this.feedType === 'newsfeed' && Math.round(scrollInfo.scrollHeight - scrollInfo.scrollTop) === scrollInfo.clientHeight && this.last) {
        this.alert.message = '더 이상 게시물이 없습니다.'
        this.alert.color = 'primary'
        this.alert.alerted = true
      }
    },
    handleScroll() {
      if (this.feedType === 'recommend' && Math.round(document.documentElement.scrollTop) + window.innerHeight === document.documentElement.offsetHeight && !this.last) {
        this.readMore() 
      } else if (this.feedType === 'recommend' && Math.round(document.documentElement.scrollTop) + window.innerHeight === document.documentElement.offsetHeight && this.last) {
        this.alert.message = '더 이상 추천 게시물이 없습니다.'
        this.alert.color = 'primary'
        this.alert.alerted = true
      }
    }
  }
}
</script>

<style scoped>
.scroll-container {
  height: calc(100vh - 160px);
}
</style>