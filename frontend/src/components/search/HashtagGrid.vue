<template>
  <v-row 
    class="scroll-container"
    
  >
    <v-col
      v-for="hashArticle in hashArticles"
      :key="hashArticle.articleId"
      class=" grid-item-padding"
      height="auto"
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
  name: 'HashtagGrid',
  data () {
    return {
      hashArticles: [],
      imageServerPrefix: `${SERVER_URL}/images/`,
      page: 0,
      size: 18,
      flag: false
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
      this.flag = true
      axios.get(`${SERVER_URL}/articles/searchbyhashtag/${this.hash}/pg?page=${this.page}&size=${this.size}`,this.getToken)
      .then((res) => {
        this.hashArticles.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
        // console.log(this.hashArticles)
      })
      .then((res) => {
        this.flag = false
      })
        
      .catch((err) => {
        alert('error' + err.message)
      })
    },
    goDetail(articleId) {

      this.$router.push({name: 'DetailArticle', params: {articleId,}})
    },
    handleScroll() {
      if (this.flag === false && Math.round(document.documentElement.scrollTop) + window.innerHeight === document.documentElement.offsetHeight && !this.last) {
        this.fetchData()
      }
    }
  },
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
  margin-bottom: 50px;
  overflow: hidden;
}

</style>