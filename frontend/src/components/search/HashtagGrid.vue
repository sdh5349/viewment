<template>
  <v-row 
    class="scroll-container"
    
  >
    <v-col
      v-for="(hashArticle,index) in hashArticles"
      :key="index"
      class="d-flex child-flex grid-item-padding"
      cols="4"
    >
     
        <v-scale-transition>
          <v-img
            :src="imageServerPrefix + hashArticle.thumbnail.path"
            :lazy-src="imageServerPrefix + hashArticle.thumbnail.path"
            aspect-ratio="1"
            @click="goDetail(hashArticle.articleId)"
          >

          <!-- 이미지 요청이 길어지면 뜨는 로딩 창 시작 -->
          <template v-slot:placeholder>
            <v-row
              class="fill-height ma-0"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="grey lighten-5"
              ></v-progress-circular>
            </v-row>
          </template>
          <!-- 이미지 요청이 길어지면 뜨는 로딩 창 끝 -->

        </v-img>
      </v-scale-transition>
    </v-col>
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
      size: 12,
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
  created() {
    window.addEventListener('scroll', this.handleScroll);
    this.fetchData()
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    fetchData() {
      this.loading = true
      axios.get(`${SERVER_URL}/articles/searchbyhashtag/${this.hash}/pg?page=${this.page}&size=${this.size}`,this.getToken)
      .then((res) => {
        console.log(res.data)
        this.hashArticles.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
        console.log(this.hashArticles)
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
    handleScroll() {
      console.log('스크롤')
      if (Math.round(document.documentElement.scrollTop) + window.innerHeight === document.documentElement.offsetHeight && !this.last) {
        console.log(document.documentElement.scrollTop)
        console.log(window.innerHeight)
        console.log(document.documentElement.offsetHeight)
        console.log(document.documentElement.scrollTop + window.innerHeight,document.documentElement.offsetHeight)
        console.log(this.hashArticles)
        this.fetchData() 
      }
    }
  },
  watch: {
    '$route': 'fetchData'
  }
}
</script>

<style scoped>
/* 그리드 각 아이템 패딩 설정 */
.grid-item-padding {
  padding: 0.3rem;
}

/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
.scroll-container {
  height: 100%;
  overflow: hidden;
  margin-bottom: 50px;
}

</style>