<template>
  <v-row 
    class="scroll-container"
  >
    <v-col
      v-for="hashArticle in hashArticles"
      :key="hashArticle.articleId"
      class="d-flex child-flex grid-item-padding"
      cols="4"
    >
        <v-scale-transition>
          <v-img
            :src="imageServerPrefix + hashArticle.thumbnail.path"
            :lazy-src="imageServerPrefix + hashArticle.thumbnail.path"
            aspect-ratio="1"
            class="grey lighten-2 image-hover"
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
      imageServerPrefix: `${SERVER_URL}/images/`
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
      axios.get(`${SERVER_URL}/articles/searchbyhashtag/`+ this.hash,this.getToken)
      .then((res) => {
        console.log(res)
        console.log(res.data)
        this.hashArticles = res.data
      })
      .catch((err) => {
        alert('error' + err.message)
      })
    },
    // getHash() {
    //   // var params = {page:0, size:10}
    //   // axios.get(`${SERVER_URL}/articles/searchbyhashtag/${this.hash}?page=0&size=0`, {params:params, headers:this.getToken.headers})
    //   // axios.get(`${SERVER_URL}/articles/searchbyhashtag/${this.hash}?page=0&size=0`,this.getToken.headers)
    //   axios.get(`${SERVER_URL}/articles/searchbyhashtag/`+this.hash ,this.getToken)
    //   .then((res) => {
    //     console.log(res)
    //     console.log(res.data)
    //     this.hashArticles = res.data
    //     this.$emit('hash', this.hash)
    //   })
    //   .catch((err) => {
    //     alert('error' + err.message)
    //   })
    // },
    goDetail(res) {
      this.$router.push({name: 'DetailArticle', params: {articleId :res,}})
    }
  },
  created() {
    this.fetchData(this.hash)
  },
  watch: {
    '$route': 'fetchData'
  }
}
</script>

<style>

</style>